package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.DowngradeableCard;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Downgrade implementation assumes that we aren't planning on downgrading a base game card. If this assumption is wrong,
 * potentially find the card from the various cardgroups and create an unupgraded instance of the card using the same uuid.
 * Downgrade implementation also assumes that we can't downgrade beyond base card (see Dicey Dungeons card downgrades).
 */
public class DowngradeCardAction extends AbstractGameAction {
    private AbstractCard card;

    public DowngradeCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        if (!(card instanceof DowngradeableCard) || !card.upgraded) {
            isDone = true;
            return;
        }
        JorbsMod.logger.info("DowngradeCardAction downgrading " + card.toString());
        ((DowngradeableCard) card).downgrade();

        Optional<AbstractCard> cardOp = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.uuid == card.uuid).findAny();
        if (cardOp.isPresent()) {
            AbstractCard masterCard = cardOp.get();
            masterCard.uuid = card.uuid;
            ((DowngradeableCard) masterCard).downgrade();
        } else {
            // we somehow tried to downgrade a card we added into the deck afterward.
            JorbsMod.logger.info("Card to downgrade is not in the deck: " + card.cardID);
        }
        isDone = true;
    }
}
