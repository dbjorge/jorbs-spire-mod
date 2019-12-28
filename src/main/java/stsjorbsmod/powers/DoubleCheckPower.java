package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.memories.DiligenceMemory;
import stsjorbsmod.memories.MemoryManager;

public class DoubleCheckPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(DoubleCheckPower.class);
    public static final String POWER_ID = STATIC.ID;

    public DoubleCheckPower(AbstractCreature owner) {
        super(STATIC);
        this.owner = owner;
    }

    @Override
    public void atStartOfTurn() {
        MemoryManager mm = MemoryManager.forPlayer(owner);
        if (mm != null && mm.isRemembering(DiligenceMemory.STATIC.ID)) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(owner));
        }

        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new DoubleCheckPower(owner);
    }
}
