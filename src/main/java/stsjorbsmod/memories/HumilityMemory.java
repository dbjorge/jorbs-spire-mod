package stsjorbsmod.memories;

import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.util.ReflectionUtils;

public class HumilityMemory extends AbstractMemory implements OnPowersModifiedSubscriber {
    public static final Logger logger = LogManager.getLogger(HumilityMemory.class.getName());
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(HumilityMemory.class);

    private static final int THORNS_ON_REMEMBER = 2;

    private int thornsAlreadyApplied;

    public HumilityMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        thornsAlreadyApplied = 0;

        setDescriptionPlaceholder("!R!", THORNS_ON_REMEMBER);
        setDescriptionPlaceholder("!P!", calculateExistingThorns());
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new ThornsPower(owner, THORNS_ON_REMEMBER)));
    }

    private int calculateExistingThorns() {
        AbstractPower maybeThornsPower = owner == null ? null : owner.getPower(ThornsPower.POWER_ID);
        int currentThornsStacks = maybeThornsPower == null ? 0 : maybeThornsPower.amount;
        int pendingThornsStacks = AbstractDungeon.actionManager.actions
                .stream()
                .filter(a ->
                        a instanceof ApplyPowerAction &&
                        a.target == owner &&
                        ReflectionUtils.getPrivateField((ApplyPowerAction) a, ApplyPowerAction.class, "powerToApply") instanceof ThornsPower)
                .mapToInt(a -> a.amount)
                .sum();

        return currentThornsStacks + pendingThornsStacks;
    }

    private void updateAppliedThorns() {
        int existingThorns = calculateExistingThorns();

        // This handles the special case of Thorns being removed entirely (eg, via Amnesia) instead of via additive modifications.
        if (existingThorns == 0 && thornsAlreadyApplied != 0) {
            logger.info("Thorns removed (Amnesia?), resetting tracked doubling state");
            thornsAlreadyApplied = 0;
        }
        
        int goalBonusThorns = existingThorns - thornsAlreadyApplied;

        // We intentionally set this to the calculated value even if we aren't applying the passive effect
        setDescriptionPlaceholder("!P!", goalBonusThorns);

        if (!isPassiveEffectActive()) {
            goalBonusThorns = 0;
        }

        int bonusThornsDelta = goalBonusThorns - thornsAlreadyApplied;
        if (bonusThornsDelta != 0) {
            logger.info(String.format(
                    "existingThorns: %1$s, thornsAlreadyApplied: %2$s, goalBonusThorns: %3$s, bonusThornsDelta: %4$s",
                    existingThorns, thornsAlreadyApplied, goalBonusThorns, bonusThornsDelta));

            AbstractPower thornsDeltaPower = new ThornsPower(owner, bonusThornsDelta);
            if (bonusThornsDelta < 0) {
                // This ensures that thorns decreases can be blocked by artifact.
                thornsDeltaPower.type = AbstractPower.PowerType.DEBUFF;
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, thornsDeltaPower, bonusThornsDelta));
            flashWithoutSound();
            thornsAlreadyApplied = goalBonusThorns;
        }
    }

    @Override
    public void onGainPassiveEffect() {
        updateAppliedThorns();
    }

    @Override
    public void onLosePassiveEffect() {
        updateAppliedThorns();
    }

    @Override
    public void receivePowersModified() {
        updateAppliedThorns();
    }
}
