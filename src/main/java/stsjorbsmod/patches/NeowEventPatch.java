package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.relics.AtStartOfActRelicSubscriber;
import stsjorbsmod.relics.BookOfTrialsRelic;

import java.util.ArrayList;

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
            };
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
    public static class NeowEvent_Cull_AddCards {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
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
        if (AbstractDungeon.player instanceof Cull) {
            AbstractDungeon.player.getRelic(BookOfTrialsRelic.ID).flash();
            ArrayList<AbstractCard> cards = DeckOfTrials.drawCards(2);
            for (AbstractCard card : cards) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }
    }
}
