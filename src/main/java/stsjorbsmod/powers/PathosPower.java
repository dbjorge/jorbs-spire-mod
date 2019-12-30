package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PathosPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PathosPower.class);
    public static final String POWER_ID = STATIC.ID;

    public PathosPower(final AbstractCreature owner, final int numberOfTurns) {
        super(STATIC);

        this.owner = owner;
        this.amount = numberOfTurns;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.flash();
        AbstractCard c = AbstractDungeon.returnRandomCurse();
        addToBot(new MakeTempCardInDiscardAction(c, 1));
    }

    @Override
    public void onInitialApplication() {

        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.setCostForTurn(-9);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[0], this.amount);
        } else {
            this.description = String.format(DESCRIPTIONS[1], this.amount);
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PathosPower(owner, amount);
    }
}

