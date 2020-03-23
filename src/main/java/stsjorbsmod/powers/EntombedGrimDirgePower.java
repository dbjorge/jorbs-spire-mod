package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExhumeCardsAction;
import stsjorbsmod.patches.EnumsPatch;

public class EntombedGrimDirgePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(EntombedGrimDirgePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static long instanceCounter = 0;

    public AbstractCreature source;
    private final AbstractCard cardToExhume;
    private final int turnToExhume;
    private boolean isFirstTurn;

    public EntombedGrimDirgePower(final AbstractCreature owner, final AbstractCard cardToExhume, final int turnToExhume) {
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

        isFirstTurn = true;
        type = EnumsPatch.SPECIAL;
        isTurnBased = false;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        // Note, the turn counter appears off by one because it isn't incremented til after start-of-turn powers are applied.
        // However, on the first turn, turn is set to 1.
        amount = turnToExhume - GameActionManager.turn - (isFirstTurn ? 0 : 1);
        updateDescription();
        isFirstTurn = false;
        if (amount <= 0) {
            flash();
            addToBot(new ExhumeCardsAction(cardToExhume));
            addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, this));
        }
    }

    @Override
    public void updateDescription() {
        if(amount == 1) {
            description = String.format(DESCRIPTIONS[0] + DESCRIPTIONS[2], cardToExhume.name, turnToExhume, amount);
        } else {
            description = String.format(DESCRIPTIONS[0] + DESCRIPTIONS[1], cardToExhume.name, turnToExhume, amount);
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new EntombedGrimDirgePower(owner, cardToExhume, amount);
    }
}
