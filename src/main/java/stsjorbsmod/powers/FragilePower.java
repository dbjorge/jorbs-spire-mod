package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

import static stsjorbsmod.patches.EnumsPatch.SPECIAL;

public class FragilePower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FragilePower.class);
    public static final String POWER_ID = STATIC.ID;

    public FragilePower(final AbstractCreature owner, final int turnsUntilSnap) {
        super(STATIC);

        this.owner = owner;
        this.amount = turnsUntilSnap;
        this.amount2 = MemoryManager.forPlayer(owner).countCurrentClarities();
        this.type = SPECIAL;

        updateDescription();
    }

    @Override
    public void onGainClarity(String memoryID) {
        this.amount2 = MemoryManager.forPlayer(owner).countCurrentClarities();
        updateDescription();
    }

    @Override
    public void onLoseClarity(String memoryID) {
        this.amount2 = MemoryManager.forPlayer(owner).countCurrentClarities();
        updateDescription();
    }

    @Override
    public void onSnap() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 1) {
            addToBot(new SnapAction(owner, true));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount, amount2 * 3, amount2 * 6);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FragilePower(owner, amount);
    }
}