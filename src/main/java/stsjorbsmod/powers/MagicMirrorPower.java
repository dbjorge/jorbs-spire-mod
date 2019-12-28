package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.HexPower;
import stsjorbsmod.JorbsMod;

public class MagicMirrorPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(MagicMirrorPower.class);
    public static final String POWER_ID = STATIC.ID;

    public MagicMirrorPower(final AbstractCreature owner, final int amount) {
        super(STATIC);

        this.owner = owner;
        this.amount = amount;

        updateDescription();
    }

    // Generally, stuff that interacts with cards will be incompatible
    private boolean isPowerIncompatibleWithMonsters(String id) {
        return
                // EntangledPower is updated in MagicMirrorPatch to be compatible
                HexPower.POWER_ID.equals(id) ||
                DrawReductionPower.POWER_ID.equals(id) ||
                ConfusionPower.POWER_ID.equals(id);
    }

    private AbstractPower tryCreateReflectedPower(AbstractCreature newTarget, AbstractCreature originalTarget, AbstractPower originalPower) {
        if (isPowerIncompatibleWithMonsters(originalPower.ID)) {
            return null;
        }

        try {
            Class<? extends AbstractPower> originalClass = originalPower.getClass();
            try {
                return originalClass.getConstructor(AbstractCreature.class, int.class, boolean.class)
                        .newInstance(newTarget, originalPower.amount, owner.isPlayer);
            } catch (NoSuchMethodException e) {}
            try {
                return originalClass.getConstructor(AbstractCreature.class, int.class)
                        .newInstance(newTarget, originalPower.amount);
            } catch (NoSuchMethodException e) {}
            try {
                return originalClass.getConstructor(AbstractCreature.class)
                        .newInstance(newTarget);
            } catch (NoSuchMethodException e) {}
            try {
                return originalClass.getConstructor(AbstractCreature.class, AbstractCreature.class, int.class)
                        .newInstance(newTarget, originalTarget, originalPower.amount);
            } catch (NoSuchMethodException e) {}
        } catch (Exception e) {
            JorbsMod.logger.error("Unable to createReflectedPower of " + originalPower, e);
        }
        return null;
    }

    public void onPowerReceived(AbstractPower originalPower) {
        if (originalPower.type == PowerType.DEBUFF) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    for (int i = 0; i < this.amount; ++i) {
                        AbstractPower reflectedPower = tryCreateReflectedPower(m, owner, originalPower);
                        if (reflectedPower != null) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, owner, reflectedPower));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new MagicMirrorPower(owner, amount);
    }
}

