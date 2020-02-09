package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.relics.RunicPyramid;

// Draw 2 cards on entering, retain 1 card per turn passively
public class DiligenceMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(DiligenceMemory.class);

    private static final int CARDS_DRAWN_ON_ENTER = 2;
    private static final int CARDS_RETAINED = 1;

    public DiligenceMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        setDescriptionPlaceholder("!D!", CARDS_DRAWN_ON_ENTER);
        setDescriptionPlaceholder("!M!", CARDS_RETAINED);
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, CARDS_DRAWN_ON_ENTER));
    }

    @Override
    public void atEndOfTurn() {
        if (isPassiveEffectActive() &&
            // Similar to Well-Laid Plans' RetainCardPower...
            !AbstractDungeon.player.hand.isEmpty() &&
            !AbstractDungeon.player.hasRelic(RunicPyramid.ID) &&
            !AbstractDungeon.player.hasPower(EquilibriumPower.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(owner, CARDS_RETAINED));
        }
    }
}
