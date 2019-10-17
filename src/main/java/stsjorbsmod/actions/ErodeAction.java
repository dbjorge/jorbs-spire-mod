package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class ErodeAction extends AbstractGameAction {
    private static final float DUR = 0.25F;
    private int poisonAmount;

    public ErodeAction(AbstractCreature target, AbstractCreature source, int loseBlockAmount, int poisonAmount) {
        this.setValues(target, source, loseBlockAmount);
        this.poisonAmount = poisonAmount;
        this.actionType = AbstractGameAction.ActionType.BLOCK;
        this.duration = DUR;
    }

    public void update() {
        if (!this.target.isDying && !this.target.isDead && this.duration == DUR && this.target.currentBlock > 0) {
            int startingBlock = this.target.currentBlock;
            this.target.loseBlock(amount);

            if (this.target.currentBlock != startingBlock) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new PoisonPower(target, source, this.poisonAmount)));
            }
        }

        this.tickDuration();
    }
}
