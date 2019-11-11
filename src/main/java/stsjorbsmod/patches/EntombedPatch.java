package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

// Entombed: "Starts combat in Exhaust pile."
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
}


