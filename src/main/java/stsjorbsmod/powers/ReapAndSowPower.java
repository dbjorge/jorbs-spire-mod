package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.characters.ReapAndSowSaveData;


public class ReapAndSowPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ReapAndSowPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static int reapAndSowDamage;

    public ReapAndSowPower(AbstractCreature owner, int amount) {
        super(STATIC);
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = reapAndSowDamage = amount;
        updateDescription();
    }

    @Override
    public void onVictory() {
        ReapAndSowSaveData.reapAndSowDamage = this.reapAndSowDamage;
        super.onVictory();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ReapAndSowPower(owner, amount);
    }
}
