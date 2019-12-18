package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class CustomJorbsModPower extends AbstractPower implements CloneablePowerInterface {
    protected final String[] DESCRIPTIONS;

    public CustomJorbsModPower(StaticPowerInfo staticPowerInfo) {
        super();
        this.ID = staticPowerInfo.ID;
        this.name = staticPowerInfo.NAME;
        this.description = staticPowerInfo.DESCRIPTIONS[0];
        this.DESCRIPTIONS = staticPowerInfo.DESCRIPTIONS;
        this.region128 = staticPowerInfo.IMG_84;
        this.region48 = staticPowerInfo.IMG_32;
    }
}

