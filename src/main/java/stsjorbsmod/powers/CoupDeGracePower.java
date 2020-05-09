package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.BlueCandle;

public class CoupDeGracePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(CoupDeGracePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static int damagePrevented = 0;

    public CoupDeGracePower(final AbstractCreature owner, final int turnsUntilDamage) {
        super(STATIC);

        this.owner = owner;
        this.isTurnBased = true;
        this.amount = turnsUntilDamage;
        this.type = PowerType.DEBUFF;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        this.description = (amount == 1 ?
                String.format(DESCRIPTIONS[0], amount, damagePrevented * 2)
                : String.format(DESCRIPTIONS[1], amount, damagePrevented * 2));
    }

    @Override
    public AbstractPower makeCopy() {
        return new CoupDeGracePower(owner, amount);
    }
}

