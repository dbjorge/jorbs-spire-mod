package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.Iterator;

public class EntombedPatch {
    public static boolean isEntombed(AbstractCard card) {
        return EntombedField.entombed.get(card);
    }
    public static CardGroup filterOutEntombedCards(CardGroup cards) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : cards.group) {
            if (!isEntombed(card)) {
                retVal.group.add(card);
            }
        }
        return retVal;
    }

    public static class TreatEntombedAsNonPurgableExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(CardGroup.class.getName()) && methodCall.getMethodName().equals("getPurgeableCards")) {
                methodCall.replace(String.format("{ $_ = %1$s.filterOutEntombedCards($proceed($$)); }", EntombedPatch.class.getName()));
            }
        }
    }

    // Remove Entombed cards from draw pile
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
                        methodCall.replace(String.format("{ if (!%1$s.isEntombed($1)) { $_ = $proceed($$); } }", EntombedPatch.class.getName()));
                    }
                }
            };
        }
    }

    // Add Entombed cards to exhaust pile
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class AbstractPlayer_preBattlePrep {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractPlayer __this) {
            for (AbstractCard c : __this.masterDeck.group) {
                if (isEntombed(c)) {
                    __this.exhaustPile.addToTop(c.makeSameInstanceOf());
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

    // Prevent Entombed cards from being bottled
    @SpirePatch(clz = BottledFlame.class, method = "onEquip")
    @SpirePatch(clz = BottledLightning.class, method = "onEquip")
    @SpirePatch(clz = BottledTornado.class, method = "onEquip")
    public static class BottledFlame_onEquip {
        public static ExprEditor Instrument() { return new TreatEntombedAsNonPurgableExprEditor(); };
    }
}


