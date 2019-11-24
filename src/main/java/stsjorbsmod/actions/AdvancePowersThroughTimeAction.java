package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Predicate;

public class AdvancePowersThroughTimeAction extends AbstractGameAction {
    Predicate<AbstractPower> shouldAffectPowerPredicate;

    public AdvancePowersThroughTimeAction(AbstractCreature target, int turnsToAdvance, Predicate<AbstractPower> shouldAffectPowerPredicate) {
        this.target = target;
        this.amount = turnsToAdvance;
        this.shouldAffectPowerPredicate = shouldAffectPowerPredicate;
    }

    @Override
    public void update() {
        for (AbstractPower power : target.powers) {
            if (shouldAffectPowerPredicate.test(power)) {
                if (target.isPlayer) {
                    power.atEndOfTurnPreEndTurnCards(true);
                    power.atEndOfTurn(true);
                }
                power.atEndOfTurnPreEndTurnCards(false);
                power.atEndOfTurn(false);
                power.atEndOfRound();
                power.atStartOfTurn();
                power.atStartOfTurnPostDraw();
            }
        }

        isDone = true;
    }
}
