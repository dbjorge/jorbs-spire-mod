package stsjorbsmod.powers;

import basemod.interfaces.AlternateCardCostModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import stsjorbsmod.actions.DecreaseMaxHpAction;

public class SacrificePower extends CustomJorbsModPower implements AlternateCardCostModifier {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(SacrificePower.class);
    public static final String POWER_ID = STATIC.ID;

    public SacrificePower(AbstractPlayer owner, int stacks) {
        super(STATIC);
        this.isTurnBased = true;
        this.amount = stacks;
        this.owner = owner;
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        --this.amount;
        if (this.amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format((this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1]), this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SacrificePower((AbstractPlayer) owner, this.amount);
    }

    @Override
    public int getAlternateResource(AbstractCard c) {
        if (c.cost == -1)
            return EnergyPanel.totalCount;
        return owner.maxHealth;
    }

    @Override
    public int spendAlternateCost(AbstractCard c, int i) {
        addToBot(new DecreaseMaxHpAction(owner, owner, i, AbstractGameAction.AttackEffect.POISON));
        return 0;
    }

    @Override
    public boolean prioritizeAlternateCost(AbstractCard card) {
        return true;
    }
}
