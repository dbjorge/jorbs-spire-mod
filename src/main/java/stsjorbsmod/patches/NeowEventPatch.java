package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;
import stsjorbsmod.relics.AtStartOfActRelicSubscriber;
import stsjorbsmod.relics.BookOfTrialsRelic;

import static stsjorbsmod.relics.BookOfTrialsRelic.addDeckOfTrialsCards;

public class NeowEventPatch {
    private static String NeowEventPatchClass = NeowEventPatch.class.getName();

    @SpirePatch(clz = NeowEvent.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
    public static class NeowEvent_ctor {
        @SpirePrefixPatch
        public static void Prefix(NeowEvent __this, boolean isDone) {
            DeckOfTrials.reset();
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
    public static class NeowEvent_ButtonEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.getClassName().equals(NeowEvent.class.getName()) && fa.getFieldName().equals("waitingToSave")) {
                        fa.replace(String.format(
                                "{ %1$s.doAtStartOfAct(); $_ = $proceed($$); }",
                                AtStartOfActRelicSubscriber.class.getName()));
                    }
                }

                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(NeowEvent.class.getName()) &&
                            (methodCall.getMethodName().equals("blessing") || methodCall.getMethodName().equals("miniBlessing"))) {
                        methodCall.replace(String.format(
                                "{ %1$s.addDeckOfTrialsCardsIfNecessary(); $_ = $proceed($$); }",
                                NeowEventPatchClass));
                    }
                }
            };
        }
    }

    public static void addDeckOfTrialsCardsIfNecessary() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof BookOfTrialsRelic) {
                addDeckOfTrialsCards();
            }
        }
    }
}
