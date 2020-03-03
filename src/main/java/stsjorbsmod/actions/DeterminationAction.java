package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import stsjorbsmod.powers.FragilePower;
import stsjorbsmod.powers.SnappedPower;

public class DeterminationAction extends AbstractGameAction {

    public DeterminationAction(AbstractCreature creature, int amount) {
        target = creature;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (target.hasPower(FragilePower.POWER_ID)) {
            FragilePower fragilePower = (FragilePower) target.getPower(FragilePower.POWER_ID);
            fragilePower.flash();
            fragilePower.amount = amount;
            fragilePower.updateDescription();
        } else if (target.hasPower(SnappedPower.POWER_ID)) {
            target.getPower(SnappedPower.POWER_ID).flash();
        } else if (!target.hasPower(FragilePower.POWER_ID) && !target.hasPower(SnappedPower.POWER_ID)) {
            addToTop(new ApplyPowerAction(target, target, new FragilePower(target, amount), amount));
        }
        isDone = true;
    }
}
