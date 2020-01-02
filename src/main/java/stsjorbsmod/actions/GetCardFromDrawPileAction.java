package stsjorbsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class GetCardFromDrawPileAction extends AbstractGameAction {
    private AbstractCard card;

    public GetCardFromDrawPileAction(AbstractCard card) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.card = card;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (AbstractDungeon.player.drawPile.contains(this.card) && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.hand.addToHand(this.card);
            this.card.unhover();
            this.card.setAngle(0.0F, true);
            this.card.lighten(false);
            this.card.drawScale = 0.12F;
            this.card.targetDrawScale = 0.75F;
            this.card.applyPowers();
            AbstractDungeon.player.drawPile.removeCard(this.card);

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.glowCheck();
        }
        this.isDone = true;
    }
}
