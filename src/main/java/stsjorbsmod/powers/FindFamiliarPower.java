package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.memories.MemoryManager;

public class FindFamiliarPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FindFamiliarPower.class);
    public static final String POWER_ID = STATIC.ID;

    public FindFamiliarPower(final AbstractCreature owner) {
        super(STATIC);
        this.owner = owner;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        MemoryManager mm = MemoryManager.forPlayer(owner);
        if (isPlayer && mm != null) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(owner));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FindFamiliarPower(owner);
    }
}
