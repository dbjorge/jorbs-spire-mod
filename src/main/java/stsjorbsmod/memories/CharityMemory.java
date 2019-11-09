package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CharityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(CharityMemory.class);

    private static final int STRENGTH_PER_GOLD_THRESHOLD = 1;
    private static final int GOLD_THRESHOLD = 100;

    private int strengthAlreadyApplied;

    public CharityMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        strengthAlreadyApplied = 0;

        setDescriptionPlaceholder("!S!", calculateBonusDamage());
        setDescriptionPlaceholder("!G!", GOLD_THRESHOLD);
    }

    private int calculateBonusDamage() {
        int gold = AbstractDungeon.player == null ? 0 : AbstractDungeon.player.gold;
        return (gold / GOLD_THRESHOLD) * STRENGTH_PER_GOLD_THRESHOLD;
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
    public void onModifyGold(AbstractPlayer player) {
        updateAppliedStrength();
    }
}
