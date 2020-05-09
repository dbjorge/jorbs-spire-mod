package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CoupDeGracePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(CoupDeGracePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static int outputDamage = 0;

    public CoupDeGracePower(final AbstractCreature owner, final int turnsUntilDamage) {
        super(STATIC);

        this.owner = owner;
        this.isTurnBased = true;
        this.amount = turnsUntilDamage;
        this.type = PowerType.DEBUFF;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        this.amount--;
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, outputDamage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            outputDamage += (damageAmount - 1) * 2;
            damageAmount = 1;
        }

        updateDescription();

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = (amount == 1 ?
                String.format(DESCRIPTIONS[0], amount, outputDamage)
                : String.format(DESCRIPTIONS[1], amount, outputDamage));
    }

    @Override
    public AbstractPower makeCopy() {
        return new CoupDeGracePower(owner, amount);
    }
}

