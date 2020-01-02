package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TroublePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(TroublePower.class);
    public static final String POWER_ID = STATIC.ID;

    public TroublePower(AbstractCreature owner, int drawPerCurse) {
        super(STATIC);

        this.owner = owner;
        this.amount = drawPerCurse;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(amount));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new TroublePower(owner, amount);
    }
}
