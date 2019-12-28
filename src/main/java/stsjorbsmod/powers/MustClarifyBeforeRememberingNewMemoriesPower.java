package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

public class MustClarifyBeforeRememberingNewMemoriesPower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(MustClarifyBeforeRememberingNewMemoriesPower.class);
    public static final String POWER_ID = STATIC.ID;

    private final String memoryIDThatMustBeClarified;

    public MustClarifyBeforeRememberingNewMemoriesPower(AbstractCreature owner, String memoryIDThatMustBeClarified) {
        super(STATIC);

        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;

        this.memoryIDThatMustBeClarified = memoryIDThatMustBeClarified;

        updateDescription();
    }

    @Override
    public boolean onRememberMemoryToCancel(String memoryIDBeingRemembered) {
        this.flash();
        return !memoryIDBeingRemembered.equals(memoryIDThatMustBeClarified);
    }

    @Override
    public void onGainClarity(String id) {
        if (id.equals(memoryIDThatMustBeClarified)) {
            // addToTop is required for cards that gain clarity and then remember something else, like EyeOfTheStorm
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public void onSnap() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MustClarifyBeforeRememberingNewMemoriesPower(owner, memoryIDThatMustBeClarified);
    }
}
