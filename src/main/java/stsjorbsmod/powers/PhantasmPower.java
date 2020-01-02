package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;

public class PhantasmPower extends CustomJorbsModPower implements OnApplyPowerToCancelSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PhantasmPower.class);
    public static final String POWER_ID = STATIC.ID;

    public PhantasmPower(AbstractCreature owner, int damageOnIntangible) {
        super(STATIC);

        this.owner = owner;
        this.amount = damageOnIntangible;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public boolean onReceivePowerToCancel(AbstractPower power, AbstractCreature source) {
        if (power.ID.equals(IntangiblePlayerPower.POWER_ID) && power.owner == this.owner) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON, true));
        }
        return false;
    }

    @Override
    public AbstractPower makeCopy() {
        return new PhantasmPower(owner, amount);
    }
}
