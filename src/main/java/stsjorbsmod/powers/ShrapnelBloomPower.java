package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.util.CardMetaUtils;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class ShrapnelBloomPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ShrapnelBloomPower.class);
    public static final String POWER_ID = STATIC.ID;

    public ShrapnelBloomPower(final AbstractCreature owner, final int additionalPlays) {
        super(STATIC);

        this.owner = owner;
        this.amount = additionalPlays;

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0 && !card.hasTag(LEGENDARY)) {
            this.flash();

            action.exhaustCard = true; // this is what corruption does
            SelfExertField.selfExert.set(card, true);

            AbstractMonster m = (AbstractMonster)action.target;

            for (int i = 0; i < amount; ++i) {
                CardMetaUtils.playCardAdditionalTime(card, m);
            }

            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }

    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[0], amount);
        } else {
            this.description = String.format(DESCRIPTIONS[1], amount);
        }
    }

    @Override
    public AbstractPower makeCopy() { return new ShrapnelBloomPower(owner, amount); }
}