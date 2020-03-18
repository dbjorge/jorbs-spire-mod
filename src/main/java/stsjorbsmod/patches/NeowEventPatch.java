package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.neow.NeowEvent;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;

public class NeowEventPatch {
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
                public void edit(FieldAccess methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(NeowEvent.class.getName()) && methodCall.getFieldName().equals("waitingToSave")) {
                        methodCall.replace(String.format(
                                "{ %1$s.addDeckOfTrialsCardsToMasterDeck(); $_ = $proceed($$); }",
                                DeckOfTrials.class.getName()));
                    }
                }
            };
        }
    }
}
