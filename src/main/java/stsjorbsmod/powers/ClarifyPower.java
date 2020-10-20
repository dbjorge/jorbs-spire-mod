package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ClarifyPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ClarifyPower.class);
    public static final String POWER_ID = STATIC.ID;

    public ClarifyPower(final AbstractCreature owner) {
        super(STATIC);

        this.owner = owner;

        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new ClarifyPower(owner);
    }
}

