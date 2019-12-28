package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpiritShieldPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(SpiritShieldPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static final int DAMAGE_REDUCTION = 1;

    public SpiritShieldPower(final AbstractCreature owner, final int numberTurns) {
        super(STATIC);

        this.owner = owner;
        this.amount = numberTurns;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            this.flash();
            return damageAmount - DAMAGE_REDUCTION;
        } else {
            return damageAmount;
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount, DAMAGE_REDUCTION);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpiritShieldPower(owner, amount);
    }
}