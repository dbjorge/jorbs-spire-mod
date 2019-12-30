package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.relics.RunicPyramid;

public class RetainCardsOnUseAction extends AbstractGameAction {
    private AbstractPlayer owner;
    private AbstractCard card;


    public RetainCardsOnUseAction(AbstractPlayer owner, int numberToRetain, AbstractCard cardUsed) {
        this.owner = owner;
        this.amount = numberToRetain;
        this.card = cardUsed;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        if (!(owner.hand.size() == 1 && owner.hand.group.get(0) == card) && !owner.hand.isEmpty() && !owner.hasRelic(RunicPyramid.ID) && !owner.hasPower(EquilibriumPower.POWER_ID)) {
            this.addToBot(new RetainCardsAction(owner, amount));
        }
        this.isDone = true;
    }
}
