package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Predicate;

/**
 * Removes all powers which test true against the predicate on the target creature.
 * For example, {@link stsjorbsmod.cards.wanderer.Amnesia} should only remove buffs and debuffs, but not the new special power type.
 *
 * Based on {@link com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction}
 * @see stsjorbsmod.cards.wanderer.Amnesia
 * @see com.megacrit.cardcrawl.powers.AbstractPower.PowerType
 */
public class RemovePowersMatchingPredicateAction extends AbstractGameAction {
    private AbstractCreature c;
    private Predicate<AbstractPower> removePowersPredicate;

    public RemovePowersMatchingPredicateAction(AbstractCreature target, Predicate<AbstractPower> removePowersPredicate) {
        super();
        this.target = target;
        this.removePowersPredicate = removePowersPredicate;
    }

    @Override
    public void update() {
        for (AbstractPower power : target.powers) {
            if (removePowersPredicate.test(power)) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, target, power.ID));
            }
        }
        this.isDone = true;
    }
}
