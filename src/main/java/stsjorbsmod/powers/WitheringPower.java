package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WitheringPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(WitheringPower.class);
    public static final String POWER_ID = STATIC.ID;

    public WitheringPower(AbstractCreature owner, int amount) {
        super(STATIC);
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = amount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
    @Override
    public AbstractPower makeCopy() {
        return new WitheringPower(owner, amount);
    }
}
