package stsjorbsmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExhumeCardsAction;

import java.util.function.Predicate;

public class ExhumeAtStartOfTurnPower extends CustomJorbsModPower implements InvisiblePower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ExhumeAtStartOfTurnPower.class);
    public static final String POWER_ID = STATIC.ID;

    private final Predicate<AbstractCard> shouldApplyToCardPredicate;

    public ExhumeAtStartOfTurnPower(AbstractCreature owner, Predicate<AbstractCard> shouldApplyToCardPredicate) {
        super(STATIC);
        this.name = "";
        this.description = "";

        this.shouldApplyToCardPredicate = shouldApplyToCardPredicate;
        this.owner = owner;
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new ExhumeCardsAction(shouldApplyToCardPredicate));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExhumeAtStartOfTurnPower(owner, shouldApplyToCardPredicate);
    }
}
