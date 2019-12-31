package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BanishedPower extends CustomJorbsModPower implements OnApplyPowerToCancelSubscriber, OnHealedBySubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(BanishedPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static final Color CREATURE_TINT = new Color(.7F,.7F,.7F,.4F);

    private AbstractCreature source;
    private AbstractPower associatedStunPower;

    public BanishedPower(final AbstractCreature owner, final AbstractCreature source, final int duration) {
        super(STATIC);
        this.isTurnBased = true;

        this.owner = owner;
        this.source = source;
        this.amount = duration;

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractMonster) {
            associatedStunPower = new StunMonsterPower((AbstractMonster)this.owner, this.amount);
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.source, associatedStunPower, this.amount));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public int onHealedBy(AbstractCreature source, int originalAmount) {
        return (source != this.owner) ? 0 : originalAmount;
    }

    @Override
    public boolean onGivePowerToCancel(AbstractPower power, AbstractCreature target) {
        return (target != this.owner);
    }

    @Override
    public boolean onReceivePowerToCancel(AbstractPower power, AbstractCreature source) {
        return (source != this.owner) && (power != associatedStunPower);
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        return 0;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        return info.owner != this.owner ? 0 : damageAmount;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        return 0;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return 0;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BanishedPower(owner, source, amount);
    }
}
