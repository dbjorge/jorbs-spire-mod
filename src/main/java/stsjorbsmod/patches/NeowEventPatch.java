package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.neow.NeowEvent;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfToils;

public class NeowEventPatch {
    @SpirePatch(clz = NeowEvent.class, method = SpirePatch.CONSTRUCTOR, paramtypez = boolean.class)
    public static class NeowEvent_ctor {
        @SpirePrefixPatch
        public static void Prefix(NeowEvent __this, boolean isDone) {
            DeckOfToils.reset();
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "buttonEffect")
    public static class NeowEvent_ButtonEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(NeowEvent.class.getName()) && methodCall.getMethodName().equals("openMap")) {
                        methodCall.replace(String.format(
                                "{ $_ = $proceed($$); %1$s.addDeckOfTrialsCardsToMasterDeck(); }",
                                DeckOfToils.class.getName()));
                    }
                }
            };
        }
    }
}
