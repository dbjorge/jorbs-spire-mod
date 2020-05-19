package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExhumeCardsAction;
import stsjorbsmod.util.CardMetaUtils;

public class OldBookPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(OldBookPower.class);
    public static final String POWER_ID = STATIC.ID;

    private AbstractCard card;

    public OldBookPower(final AbstractCreature owner, AbstractCard card) {
        super(STATIC);

        this.isTurnBased = false;
        this.card = card;
        this.owner = owner;

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount >= owner.currentHealth) {
            info.output = damageAmount = 0;
            addToBot(new ExhumeCardsAction(card));
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));

            if (card.upgraded) {
                float percent = (float)card.magicNumber / 100.0F;
                int healAmt = (int)((float)owner.maxHealth * percent);
                if (healAmt < 1) {
                    healAmt = 1;
                }

                owner.heal(healAmt, true);
            }

            CardMetaUtils.destroyCardPermanently(card);
        }

        updateDescription();
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        if (card.upgraded)
            description = String.format(DESCRIPTIONS[1], card.magicNumber);
        else
            description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new OldBookPower(owner, card);
    }
}
