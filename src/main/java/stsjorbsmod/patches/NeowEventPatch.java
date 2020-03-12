package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.neow.NeowEvent;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;

public class NeowEventPatch {
    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
    public static class NeowEvent_ButtonEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractEvent.class.getName()) && methodCall.getMethodName().equals("openMap")) {
                        methodCall.replace(String.format(
                                "{ $_ = $proceed($$); %1$s.addDeckOfTrialsCardsToMasterDeck(); }",
                                DeckOfTrials.class.getName()));
                    }
                }
            };
        }
    }
}
