package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class GainBlockPerPoisonAction extends AbstractGameAction  {
    private AbstractCreature owner;
    private AbstractCreature poisonee;

    public GainBlockPerPoisonAction(AbstractCreature owner, AbstractCreature poisonee) {
        this.owner = owner;
        this.poisonee = poisonee;
        this.actionType = ActionType.BLOCK;
    }

    public void update() {
        final AbstractPower poisonPower = poisonee.getPower(PoisonPower.POWER_ID);
        final int block = poisonPower != null
                ? poisonPower.amount
                : 0;
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(owner, owner, block));
        this.isDone = true;
    }
}
