package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReducedHealingPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ReducedHealingPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static final int HEAL_REDUCTION_PERCENTAGE = 100;

    private AbstractCreature source;

    public ReducedHealingPower(AbstractCreature owner, AbstractCreature source) {
        super(STATIC);

        this.type = PowerType.DEBUFF;

        this.owner = owner;
        this.source = source;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
            // "Reduce healing by %1$s%."
            this.description = String.format(DESCRIPTIONS[0], HEAL_REDUCTION_PERCENTAGE);
    }

    @Override
    public int onHeal(int healAmount) {
        return 0;
    }

    @Override
    public void onRemove() {
        if (owner instanceof TimeEater) {
            // This should happen after ReducedHealing gets removed, but before the healing action.
            AbstractDungeon.actionManager.actions.forEach(a -> updateHealing(a, owner));
        }
    }

    private static void updateHealing(AbstractGameAction action, AbstractCreature c) {
        if (action instanceof HealAction && action.target == c && action.source == c) {
            action.amount = 0;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ReducedHealingPower(this.owner, this.source);
    }

}
