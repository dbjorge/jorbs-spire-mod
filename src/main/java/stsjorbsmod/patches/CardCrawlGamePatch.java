package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;

public class CardCrawlGamePatch {

    @SpirePatch(clz = CardCrawlGame.class, method = "update")
    public static class CardCrawlGame_Update {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(DungeonMapScreen.class.getName()) && methodCall.getMethodName().equals("open")) {
                        methodCall.replace(String.format(
                                "{ %1$s.addDeckOfTrialsCardsToMasterDeck(); $_ = $proceed($$); }",
                                DeckOfTrials.class.getName()));
                    }
                }
            };
        }
    }
}
