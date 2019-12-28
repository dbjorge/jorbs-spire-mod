package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.SnapAction;

public class FragilePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FragilePower.class);
    public static final String POWER_ID = STATIC.ID;

    public FragilePower(final AbstractCreature owner, final int turnsUntilSnap) {
        super(STATIC);

        this.owner = owner;
        this.amount = turnsUntilSnap;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new SnapAction(owner, true));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FragilePower(owner, amount);
    }
}