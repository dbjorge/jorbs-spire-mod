package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AdvancePowersThroughTimeAction extends AbstractGameAction {
    Predicate<AbstractPower> shouldAffectPowerPredicate;

    public AdvancePowersThroughTimeAction(AbstractCreature target, int turnsToAdvance, Predicate<AbstractPower> shouldAffectPowerPredicate) {
        this.target = target;
        this.amount = turnsToAdvance;
        this.shouldAffectPowerPredicate = shouldAffectPowerPredicate;
    }

    private void forEachApplicablePower(Consumer<AbstractPower> callback) {
        for (AbstractPower power : target.powers) {
            if (shouldAffectPowerPredicate.test(power)) {
                callback.accept(power);
            }
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; ++i) {
            forEachApplicablePower(p -> p.atEndOfTurnPreEndTurnCards(target.isPlayer));
            forEachApplicablePower(p -> p.atEndOfTurn(target.isPlayer));
            forEachApplicablePower(p -> p.atEndOfRound());
            forEachApplicablePower(p -> p.atStartOfTurn());
            forEachApplicablePower(p -> p.atStartOfTurnPostDraw());
        }
        
        isDone = true;
    }
}
