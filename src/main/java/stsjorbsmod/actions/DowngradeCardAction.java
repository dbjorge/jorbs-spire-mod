package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.DowngradeableCard;
import stsjorbsmod.cards.wanderer.Patron;

public class DowngradeCardAction extends AbstractGameAction {
    private DowngradeableCard card;

    public DowngradeCardAction(DowngradeableCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        JorbsMod.logger.info("DowngradeCardAction downgrading " + card.toString());
        card.downgrade();
        isDone = true;
    }
}
