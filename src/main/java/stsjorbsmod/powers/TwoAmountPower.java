package stsjorbsmod.powers;

import stsjorbsmod.JorbsMod;

public class TwoAmountPower extends com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower {
    public void stackSecondPower(int stackAmount) {
        if (this.amount2 == -1) {
            JorbsMod.logger.info(this.name + " does not stack");
        } else {
            this.fontScale = 8.0F;
            this.amount2 += stackAmount;
        }
    }

    public void reduceSecondPower(int reduceAmount) {
        if (this.amount2 - reduceAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount2 = 0;
        } else {
            this.fontScale = 8.0F;
            this.amount2 -= reduceAmount;
        }
    }
}
