package stsjorbsmod.util;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.DowngradeableCard;

public class CardMetaUtils {
    /**
     * Downgrade implementation assumes that we are planning on downgrading only Jorbs mod game cards. If this assumption is
     * wrong, potentially find the card from the various cardgroups and create an unupgraded instance of the card using the
     * same uuid. For this mod at the moment, Wrath stacks would likely need to carry over properly.
     * Nice to have: handle downgrading cards from other mods with unknown effects.
     * Downgrade implementation also assumes that we can't downgrade beyond base card (see Dicey Dungeons card downgrades).
     */
    public static void downgradePermanently(AbstractCard card, float duration) {
        if (!card.purgeOnUse && (card instanceof DowngradeableCard) && card.upgraded) {
            JorbsMod.logger.info("Downgrading " + card.toString());
            ((DowngradeableCard) card).downgrade();

            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
            if (masterCard != null) {
                ((DowngradeableCard) masterCard).downgrade();
            } else {
                // we somehow tried to downgrade a card we added into the deck afterward.
                JorbsMod.logger.info("Card to downgrade is not in the deck: " + card.cardID);
            }
        }
    }

    public static void removeCard(AbstractCard card) {
        card.purgeOnUse = true; // handles destroying the copy that's in the middle of being played
        AbstractDungeon.player.discardPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
        AbstractDungeon.player.drawPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
        AbstractDungeon.player.exhaustPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
        AbstractDungeon.player.hand.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));

        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if (masterCard != null) {
            JorbsMod.logger.info("Removing " + masterCard.toString());
            AbstractDungeon.player.masterDeck.removeCard(masterCard);
        } else {
            JorbsMod.logger.info("Failed to purge a card we didn't have. Perhaps Duplication Potion or Attack Potion or similar effect occurred. " + card.cardID);
        }
    }
}
