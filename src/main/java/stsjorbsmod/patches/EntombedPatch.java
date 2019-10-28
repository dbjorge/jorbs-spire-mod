package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.actions.ExhumeEntombedCardsAction;

// Entombed: "Starts combat in Exhaust pile. Move from Exhaust pile to hand when an enemy dies."
// Implementation inspired by StSLib's GraveField
public class EntombedPatch {
    @SpirePatch(
            clz = CardGroup.class,
            method = "initializeDeck"
    )
    public static class EntombedInitializeDeckPatch {
        @SpireInsertPatch(
                rloc = 4,
                localvars = {"copy"}
        )
        public static void Insert(CardGroup __instance, CardGroup masterDeck, CardGroup copy) {
            for (AbstractCard c : copy.group) {
                if (EntombedField.entombed.get(c)) {
                    AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = { boolean.class }
    )
    public static class EntombedOnMonsterDeathPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __this) {
            if ((!__this.isDying && __this.currentHealth <= 0) && !__this.halfDead) {
                AbstractDungeon.actionManager.addToBottom(new ExhumeEntombedCardsAction());
            }
        }
    }
}


