package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import stsjorbsmod.memories.SnapCounter;
import stsjorbsmod.powers.FragilePower;
import stsjorbsmod.powers.SnappedPower;

public class DeterminationAction extends AbstractGameAction {

    public DeterminationAction(AbstractPlayer creature, int amount) {
        target = creature;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (target.hasPower(FragilePower.POWER_ID)) {
            FragilePower fragilePower = (FragilePower) target.getPower(FragilePower.POWER_ID);
            fragilePower.forceSnapTurn();
        } else if (target.hasPower(SnappedPower.POWER_ID)) {
            target.getPower(SnappedPower.POWER_ID).flash();
        } else if (!target.hasPower(FragilePower.POWER_ID) && !target.hasPower(SnappedPower.POWER_ID)) {
            SnapCounter snapCounter = new SnapCounter((AbstractPlayer) target);
            snapCounter.forceSnapTurn();
            addToTop(new ApplyPowerAction(target, target, new FragilePower(target, snapCounter), amount));
        }
        isDone = true;
    }
}
