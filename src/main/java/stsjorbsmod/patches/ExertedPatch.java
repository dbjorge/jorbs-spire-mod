package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ExertedPatch {
    public static boolean isExerted(AbstractCard card) {
        return ExertedField.exerted.get(card);
    }

    // Remove Exerted cards from draw pile
    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class CardGroup_initializeDeck {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(CardGroup.class.getName()) && methodCall.getMethodName().equals("addToTop")) {
                        methodCall.replace(String.format("{ if (!%1$s.isExerted($1)) { $_ = $proceed($$); } }", ExertedPatch.class.getName()));
                    }
                }
            };
        }
    }

    // Add Exerted cards to exhaust pile
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class AbstractPlayer_preBattlePrep {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractPlayer __this) {
            for (AbstractCard c : __this.masterDeck.group) {
                if (isExerted(c)) {
                    AbstractCard card = c.makeSameInstanceOf();
                    __this.exhaustPile.addToTop(card);
                    ExertedField.exertedAtStartOfCombat.set(card, true);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isEndingTurn");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}


