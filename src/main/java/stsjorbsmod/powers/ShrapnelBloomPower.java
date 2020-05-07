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

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class ShrapnelBloomPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ShrapnelBloomPower.class);
    public static final String POWER_ID = STATIC.ID;
    private int numPlays;

    public ShrapnelBloomPower(final AbstractCreature owner, final int amount, int numPlays) {
        super(STATIC);

        this.owner = owner;
        this.amount = amount;
        this.numPlays = numPlays;

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0 && !card.hasTag(LEGENDARY)) {
            this.flash();

            action.exhaustCard = true; // this is what corruption does
            SelfExertField.selfExert.set(card, true);

            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            // play numPlays - 1, because one is the "real" card
            for (int i = 0; i < numPlays - 1; i++) {
                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            }

            --this.amount;
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
            else {
                updateDescription();
            }
        }

    }

    @Override
    public void atEndOfRound() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if (this.amount == 1) {
            this.description += String.format(DESCRIPTIONS[1], numPlays);
        } else {
            this.description += String.format(DESCRIPTIONS[2], this.amount, numPlays);
        }
    }

    @Override
    public AbstractPower makeCopy() { return new ShrapnelBloomPower(owner, amount, numPlays); }
}