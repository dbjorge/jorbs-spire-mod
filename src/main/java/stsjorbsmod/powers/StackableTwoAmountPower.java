package stsjorbsmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;

public class StackableTwoAmountPower extends TwoAmountPower implements CustomStackBehaviorPower {
    public void stackPower(AbstractPower otherPower) {
        this.stackPower(otherPower.amount);

        if (this.amount2 == -1) {
            JorbsMod.logger.info(this.name + " does not stack");
        } else {
            this.fontScale = 8.0F;
            this.amount2 += ((StackableTwoAmountPower) otherPower).amount2;
        }
    }
}
