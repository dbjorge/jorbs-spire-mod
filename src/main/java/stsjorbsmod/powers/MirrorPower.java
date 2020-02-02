package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MirrorPower extends CustomJorbsModPower implements OnDamageToRedirectSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(MirrorPower.class);
    public static final String POWER_ID = STATIC.ID;

    public MirrorPower(final AbstractCreature owner, final int numberTurns) {
        super(STATIC);

        this.owner = owner;
        this.amount = numberTurns;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public boolean onDamageToRedirect(AbstractPlayer player, DamageInfo info, AbstractGameAction.AttackEffect attackEffect) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.flash();
            this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, info.output, DamageInfo.DamageType.THORNS), attackEffect, false));
            return true;
        }
        return false;
    }

    @Override
    public int onLoseHp(int damageAmount) {
            return 0;
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new MirrorPower(owner, amount);
    }
}