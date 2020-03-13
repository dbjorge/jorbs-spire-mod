package stsjorbsmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExhumeCardsAction;


public class ExhumeOnTurnXPower extends CustomJorbsModPower implements InvisiblePower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ExhumeOnTurnXPower.class);
    public static final String POWER_ID = STATIC.ID;
    private final AbstractCard cardToExhume;

    private final int EXHUME_TURN;
    private boolean isFirstTurn;

    public ExhumeOnTurnXPower(AbstractCreature owner, final AbstractCard cardToExhume, int turn) {
        super(STATIC);
        this.name = "";
        this.description = "";
        EXHUME_TURN = turn;

        this.cardToExhume = cardToExhume;
        this.owner = owner;
    }

    @Override
    public void atStartOfTurn() {
        amount = EXHUME_TURN - GameActionManager.turn - (isFirstTurn ? 0 : 1);
        updateDescription();
        isFirstTurn = false;
        if (amount == 0) {
            flash();
            addToBot(new ExhumeCardsAction(cardToExhume));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExhumeOnTurnXPower(owner, cardToExhume, EXHUME_TURN);
    }
}
