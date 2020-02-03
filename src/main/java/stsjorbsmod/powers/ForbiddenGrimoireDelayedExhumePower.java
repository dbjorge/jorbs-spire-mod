package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExhumeCardsAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

public class ForbiddenGrimoireDelayedExhumePower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ForbiddenGrimoireDelayedExhumePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static long instanceCounter = 0;

    public AbstractCreature source;
    private final AbstractCard cardToExhume;

    public ForbiddenGrimoireDelayedExhumePower(final AbstractCreature owner, final AbstractCard cardToExhume, final int cardPlaysUntilExhume) {
        super(STATIC);

        // This prevents the power from stacking with other instances of itself for different card instances.
        // This is the same strategy used by TheBombPower.
        //
        // StSLib provides a NonStackablePower interface with similar functionality, but we're intentionally not using
        // it because it is hackier than the ID thing.
        ID = POWER_ID + "__" + (++instanceCounter);

        this.owner = owner;
        this.amount = cardPlaysUntilExhume;
        this.source = owner;

        this.cardToExhume = cardToExhume;

        type = PowerType.BUFF;
        isTurnBased = false;

        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (amount <= 1) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ExhumeCardsAction(cardToExhume));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format((amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1]), cardToExhume.name, amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ForbiddenGrimoireDelayedExhumePower(owner, cardToExhume, amount);
    }
}
