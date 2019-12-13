package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SlothMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(SlothMemory.class);

    private static final int DISCARD_ON_REMEMBER = 3;
    private static final int DRAW_REDUCTION = 1;

    public SlothMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.SIN, owner);
        setDescriptionPlaceholder("!D!", DISCARD_ON_REMEMBER);
        setDescriptionPlaceholder("!M!", DRAW_REDUCTION);
    }

    @Override
    public void onRemember() {
        // addToTop is required for correct interaction with Unseen Servant
        AbstractDungeon.actionManager.addToTop(
                new DiscardAction(owner, owner, DISCARD_ON_REMEMBER, true));
    }

    @Override
    public void onGainPassiveEffect() {
        // We modify gameHandSize directly rather than using a DrawReductionPower because we don't want
        // the effect to be prevented by artifact.
        --AbstractDungeon.player.gameHandSize;
    }

    @Override
    public void onLosePassiveEffect() {
        ++AbstractDungeon.player.gameHandSize;
    }
}
