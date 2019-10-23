package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

// Draw 2 cards on entering, retain 1 card per turn passively
public class DiligenceMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(DiligenceMemory.class);

    private static final int CARDS_DRAWN_ON_ENTER = 2;
    private static final int CARDS_RETAINED = 1;

    // Set to true if the memory is gained after the end-of-turn trigger has already passed,
    // but we still need to process the end-of-turn effects for this memory.
    private boolean isTurnAlreadyEnding;

    public DiligenceMemory(final AbstractCreature owner, boolean isClarified, boolean isTurnAlreadyEnding) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        setDescriptionPlaceholder("!M!", CARDS_RETAINED);
        this.isTurnAlreadyEnding = isTurnAlreadyEnding;
    }

    public DiligenceMemory(final AbstractCreature owner, boolean isClarified) {
        this(owner, isClarified, false);
    }

    @Override
    public void onRemember() {
        // isTurnAlreadyEnding is a special case for effects like SharpenedMindPower that "remember Diligence" as an
        // end-of-turn effect. Since atEndOfTurn triggers have already gone off by the time the action remembering
        // the memory goes off, atEndOfTurn won't trigger in that case, and since the action discarding your hand at
        // end of turn is already on the action queue, we need to add both the onRemember draw action and passive
        // Retain effect to the top of the action queue.
        //
        // Because they both have to go on top and the DrawCardAction has to end up before the retain, we can't use
        // onGainPassiveEffect for the retain action (the ordering would be backwards). Not using onGainPassiveEffect
        // means the MemoryManager can't deal with avoiding the double passive effect if the player has both memory and
        // clarity at the same time, so we manually check for the Clarity existing already to avoid that.
        if (isTurnAlreadyEnding) {
            if (!MemoryManager.forPlayer(owner).hasClarity(this.ID)) {
                AbstractDungeon.actionManager.addToTop(new RetainCardsAction(owner, CARDS_RETAINED));
            }
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(owner, CARDS_DRAWN_ON_ENTER));
            isTurnAlreadyEnding = false;
        } else {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(owner, CARDS_DRAWN_ON_ENTER));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPassiveEffectActive && isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(owner, CARDS_RETAINED));
        }
    }

    @Override
    public AbstractMemory makeCopy() {
        return new DiligenceMemory(owner, isClarified, isTurnAlreadyEnding);
    }
}
