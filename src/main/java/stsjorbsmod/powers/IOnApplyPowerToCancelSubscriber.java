package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface IOnApplyPowerToCancelSubscriber {
    // return true to cancel receiving the power
    default boolean onReceivePowerToCancel(AbstractPower power, AbstractCreature source) { return false; };

    // return true to cancel giving the power
    default boolean onGivePowerToCancel(AbstractPower power, AbstractCreature target) { return false; };
}
