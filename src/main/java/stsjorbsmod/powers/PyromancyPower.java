package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PyromancyPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PyromancyPower.class);
    public static final String POWER_ID = STATIC.ID;

    public PyromancyPower(final AbstractCreature owner, final int numExtraTimesToBurn) {
        super(STATIC);

        this.owner = owner;
        this.amount = numExtraTimesToBurn;

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (source == owner && power.ID.equals(BurningPower.POWER_ID) && !((BurningPower)power).generatedByPyromancy) {
            for (int i = 0; i < amount; ++i) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new BurningPower(target, source, power.amount, true)));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new PyromancyPower(owner, amount);
    }
}

