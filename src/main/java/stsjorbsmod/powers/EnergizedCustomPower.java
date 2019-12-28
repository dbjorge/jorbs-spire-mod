package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EnergizedCustomPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(EnergizedCustomPower.class);
    public static final String POWER_ID = STATIC.ID;

    public EnergizedCustomPower(AbstractCreature owner, int energyAmt) {
        super(STATIC);

        this.owner = owner;
        this.amount = energyAmt;
        if (this.amount >= 999) {
            this.amount = 999;
        }

        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= 999) {
            this.amount = 999;
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void onEnergyRecharge() {
        this.flash();
        AbstractDungeon.player.gainEnergy(this.amount);
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public AbstractPower makeCopy() {
        return new EnergizedCustomPower(owner, amount);
    }
}
