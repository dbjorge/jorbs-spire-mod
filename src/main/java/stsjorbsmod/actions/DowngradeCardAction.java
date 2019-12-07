package stsjorbsmod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.DowngradeableCard;

/**
 * Downgrade implementation assumes that we are planning on downgrading only Jorbs mod game cards. If this assumption is
 * wrong, potentially find the card from the various cardgroups and create an unupgraded instance of the card using the
 * same uuid. For this mod at the moment, Wrath stacks would likely need to carry over properly.
 * Nice to have: handle downgrading cards from other mods with unknown effects.
 * Downgrade implementation also assumes that we can't downgrade beyond base card (see Dicey Dungeons card downgrades).
 */
public class DowngradeCardAction extends AbstractGameAction {
    private AbstractCard card;

    public DowngradeCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        if ((card instanceof DowngradeableCard) && card.upgraded) {
            JorbsMod.logger.info("DowngradeCardAction downgrading " + card.toString());
            ((DowngradeableCard) card).downgrade();

            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
            if (masterCard != null) {
                ((DowngradeableCard) masterCard).downgrade();
            } else {
                // we somehow tried to downgrade a card we added into the deck afterward.
                JorbsMod.logger.info("Card to downgrade is not in the deck: " + card.cardID);
            }
        }
        isDone = true;
    }
}
