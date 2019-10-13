package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.AbstractMemoryPower;
import stsjorbsmod.powers.SnappedPower;


// This is like ApplyPowerAction, but with the additional effect of removing other non-clarified memories
public class RememberSpecificMemoryAction extends AbstractGameAction  {
    private AbstractMemoryPower memoryToRemember;

    public RememberSpecificMemoryAction(AbstractCreature target, AbstractCreature source, AbstractMemoryPower memoryToRemember) {
        this.setValues(target, source);
        this.memoryToRemember = memoryToRemember;
    }

    public void update() {
        if (target.hasPower(SnappedPower.POWER_ID)) {
            target.getPower(SnappedPower.POWER_ID).flash();
            isDone = true;
            return;
        }

        // Regardless of whether the old memory is clarified or not, re-remembering it is a no-op
        if (target.hasPower(memoryToRemember.ID)) {
            target.getPower(memoryToRemember.ID).flashWithoutSound();
            isDone = true;
            return;
        }

        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, memoryToRemember));

        for (AbstractPower oldPower : this.source.powers) {
            if (oldPower instanceof AbstractMemoryPower) {
                AbstractMemoryPower oldMemory = (AbstractMemoryPower) oldPower;
                if (!oldMemory.isClarified) {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, oldMemory));
                }
            }
        }

        isDone = true;
    }
}
