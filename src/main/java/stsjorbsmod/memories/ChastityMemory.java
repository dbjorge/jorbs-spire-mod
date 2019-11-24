package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class ChastityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(ChastityMemory.class);

    private static final int DEXTERITY_ON_REMEMBER = 2;
    private static final int DEXTERITY_LOSS_PER_TURN = 1;
    private static final int BLOCK_PER_TURN = 6;

    public ChastityMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        setDescriptionPlaceholder("!D!", DEXTERITY_LOSS_PER_TURN);
        setDescriptionPlaceholder("!B!", BLOCK_PER_TURN);
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new DexterityPower(owner, DEXTERITY_ON_REMEMBER), DEXTERITY_ON_REMEMBER));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPassiveEffectActive()) {
            AbstractDungeon.actionManager.addToBottom(
                    // It's important to apply a negative dex power rather than reducing an existing dex power
                    //   1. reducing the existing power doesn't work if the player currently has no dex
                    //   2. we want it to be blockable by Artifact, which ApplyPowerAction is and ReducePowerAction isn't
                    new ApplyPowerAction(owner, owner, new DexterityPower(this.owner, -this.DEXTERITY_LOSS_PER_TURN)));
            AbstractDungeon.actionManager.addToBottom(
                    new GainBlockAction(owner, owner, BLOCK_PER_TURN));
        }
    }
}
