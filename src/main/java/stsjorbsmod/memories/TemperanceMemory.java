package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class TemperanceMemory extends AbstractMemory implements OnModifyMemoriesSubscriber {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(TemperanceMemory.class);

    private static final int STRENGTH_PER_CLARITY = 1;

    private int strengthAlreadyApplied;

    public TemperanceMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        strengthAlreadyApplied = 0;

        setDescriptionPlaceholder("!S!", calculateBonusDamage());
    }

    private int calculateBonusDamage() {
        MemoryManager memoryManager = MemoryManager.forPlayer(owner);
        int numCharities = memoryManager == null ? 0 : memoryManager.countCurrentClarities();
        return numCharities * STRENGTH_PER_CLARITY;
    }

    private void updateAppliedStrength() {
        int newStrength = calculateBonusDamage();

        // We intentionally set this to the calculated value even if we aren't applying the passive effect
        setDescriptionPlaceholder("!S!", newStrength);

        if (!isPassiveEffectActive()) {
            newStrength = 0;
        }

        int strengthDelta = newStrength - strengthAlreadyApplied;
        if (strengthDelta != 0) {
            // It is by design that strength decreases can be blocked by artifact.
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthDelta), strengthDelta));
            flashWithoutSound();
            strengthAlreadyApplied = newStrength;
        }
    }

    @Override
    public void onGainPassiveEffect() {
        updateAppliedStrength();
    }

    @Override
    public void onLosePassiveEffect() {
        updateAppliedStrength();
    }

    @Override
    public void onGainClarity(String id) {
        updateAppliedStrength();
    }

    @Override
    public void onLoseClarity(String id) {
        updateAppliedStrength();
    }
}
