package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExhumeCardsAction;

public class SummoningPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(SummoningPower.class);
    public static final String POWER_ID = STATIC.ID;

    public AbstractCard card;
    private static long instanceCounter = 0;


    public SummoningPower(AbstractCreature owner, AbstractCard c) {
        super(STATIC);

        ID = POWER_ID + "__" + (++instanceCounter);
        this.card = c;
        this.owner = owner;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new ExhumeCardsAction(card));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], card.name);
    }


    @Override
    public AbstractPower makeCopy() {
        return new SummoningPower(owner, card);
    }
}
