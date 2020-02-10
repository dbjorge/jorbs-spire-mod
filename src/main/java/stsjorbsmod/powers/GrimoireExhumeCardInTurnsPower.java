package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.memories.SnapCounter;
import stsjorbsmod.patches.EnumsPatch;

public class GrimoireExhumeCardInTurnsPower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(GrimoireExhumeCardInTurnsPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static long instanceCounter = 0;

    public AbstractCreature source;
    private final AbstractCard cardToExhume;
    private final int turnToExhume;
    private SnapCounter snapCounter;

    public GrimoireExhumeCardInTurnsPower(final AbstractCreature owner, final AbstractCard cardToExhume, final int turnToExhume, final SnapCounter snapCounter) {
        super(STATIC);

        // This prevents the power from stacking with other instances of itself for different card instances.
        // This is the same strategy used by TheBombPower.
        //
        // StSLib provides a NonStackablePower interface with similar functionality, but we're intentionally not using
        // it because it is hackier than the ID thing.
        ID = POWER_ID + "__" + (++instanceCounter);

        this.owner = owner;
        this.source = owner;
        this.amount = turnToExhume;

        this.cardToExhume = cardToExhume;
        this.turnToExhume = turnToExhume;
        this.snapCounter = snapCounter;

        type = EnumsPatch.SPECIAL;
        isTurnBased = false;

        updateDescription();
    }

    private void exhume() {
        this.flash();
//            addToBot(new ExhumeCardsAction(cardToExhume));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void atStartOfTurn() {
        this.amount = turnToExhume - snapCounter.getCurrentTurn();
        if (amount <= 0) {
            exhume();
        }
    }

    @Override
    public void onSnap() {
        exhume();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], cardToExhume.name, turnToExhume);
    }

    @Override
    public AbstractPower makeCopy() {
        return new GrimoireExhumeCardInTurnsPower(owner, cardToExhume, amount, snapCounter);
    }
}
