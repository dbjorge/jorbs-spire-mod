package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.util.CardMetaUtils;

public class DoubleExhaustPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(DoubleExhaustPower.class);
    public static final String POWER_ID = STATIC.ID;

    public DoubleExhaustPower(final AbstractCreature owner, final int cardsPerTurn) {
        super(STATIC);

        this.owner = owner;
        this.amount = cardsPerTurn;

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && card.exhaust == true && this.amount > 0) {
            this.flash();

            CardMetaUtils.playCardAdditionalTime(card, (AbstractMonster) action.target);

            --this.amount;
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                updateDescription();
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }

    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = String.format(DESCRIPTIONS[1], this.amount);
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new DoubleExhaustPower(owner, amount);
    }
}

