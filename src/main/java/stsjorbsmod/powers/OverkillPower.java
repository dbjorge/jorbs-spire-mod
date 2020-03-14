package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class OverkillPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(OverkillPower.class);
    public static final String POWER_ID = STATIC.ID;

    public OverkillPower(AbstractCreature owner, int amount) {
        super(STATIC);
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if(amount == 0) {
            this.description = String.format(DESCRIPTIONS[0]);
        }
        else {
            this.description = String.format(DESCRIPTIONS[1], this.amount);
        }
    }


    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(damageAmount > target.currentHealth){
            int overkill_damage = damageAmount - target.currentHealth;
            addToBot(new DamageAllEnemiesAction(null,
                    DamageInfo.createDamageMatrix(overkill_damage + this.amount),
                    DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new StrengthNextTurnPower(owner, amount);
    }
}

