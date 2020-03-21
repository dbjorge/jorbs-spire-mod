package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FindWeaknessPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FindWeaknessPower.class);
    public static final String POWER_ID = STATIC.ID;

    private boolean attackPlayedThisTurn;

    public FindWeaknessPower(final AbstractCreature owner, final int scryAmount) {
        super(STATIC);

        this.owner = owner;
        this.amount = scryAmount;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && !this.attackPlayedThisTurn) {
            attackPlayedThisTurn = true;
            this.flash();
            this.updateDescription();

            addToBot(new ScryAction(this.amount));
        }
    }

    public void atStartOfTurn() {
        this.attackPlayedThisTurn = false;
    }


    @Override
    public void updateDescription() {
            this.description = String.format((!this.attackPlayedThisTurn ? DESCRIPTIONS[0] : DESCRIPTIONS[1]), this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FindWeaknessPower(owner, amount);
    }
}

