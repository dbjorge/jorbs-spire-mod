package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StrangePendantPower extends CustomJorbsModPower implements OnPlayerHpLossPowerSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(StrangePendantPower.class);
    public static final String POWER_ID = STATIC.ID;

    private int damageAbsorbedThisTurn = 0;

    public StrangePendantPower(AbstractCreature owner, int damageReduction) {
        super(STATIC);

        this.owner = owner;
        this.amount = damageReduction;

        this.updateDescription();
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void atStartOfTurn() {
        this.damageAbsorbedThisTurn = 0;
    }

    @Override
    public int onPlayerHpLoss(int originalHpLoss) {
        int remainingAbsorb = amount - damageAbsorbedThisTurn;
        if (remainingAbsorb <= 0 || originalHpLoss <= 0) { return originalHpLoss; }

        this.flash();
        int damageToAbsorb = Math.min(originalHpLoss, remainingAbsorb);
        damageAbsorbedThisTurn += damageToAbsorb;
        return originalHpLoss - damageToAbsorb;
    }

    @Override
    public AbstractPower makeCopy() {
        return new StrangePendantPower(owner, amount);
    }
}
