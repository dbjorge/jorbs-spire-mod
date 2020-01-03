package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.relics.RunicPyramid;

public class RetainCardsPolitelyAction extends AbstractGameAction {
    private AbstractPlayer owner;

    public RetainCardsPolitelyAction(AbstractPlayer owner, int numberToRetain) {
        this.owner = owner;
        this.amount = numberToRetain;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        // Note: when a card's use() adds this action to the queue, that card will already be
        // removed from "hand" (and in "limbo" instead) before reaching this point.
        if (!owner.hand.isEmpty() && !owner.hasRelic(RunicPyramid.ID) && !owner.hasPower(EquilibriumPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new RetainCardsAction(owner, amount));
        }
        this.isDone = true;
    }
}
