package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.util.CardMetaUtils;

// Similar to DoubleTapPower, except stacks as "next card plays N times" rather than "next N attacks are played twice"
public class PlayNextCardThisTurnAdditionalTimesPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PlayNextCardThisTurnAdditionalTimesPower.class);
    public static final String POWER_ID = STATIC.ID;

    public PlayNextCardThisTurnAdditionalTimesPower(final AbstractCreature owner, final int additionalTimes) {
        super(STATIC);

        this.owner = owner;
        this.amount = additionalTimes;

        updateDescription();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0) {
            this.flash();

            AbstractMonster m = (AbstractMonster)action.target;

            for (int i = 0; i < amount; ++i) {
                CardMetaUtils.playCardAdditionalTime(card, m);
            }

            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
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
        description = amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);;
    }

    @Override
    public AbstractPower makeCopy() {
        return new PlayNextCardThisTurnAdditionalTimesPower(owner, amount);
    }
}

