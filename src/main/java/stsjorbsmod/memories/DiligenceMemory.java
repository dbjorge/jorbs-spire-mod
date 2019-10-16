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
public class DiligenceMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int CARDS_DRAWN_ON_ENTER = 2;
    private static final int CARDS_RETAINED = 1;

    public static final String POWER_ID = JorbsMod.makeID(DiligenceMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("diligence_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("diligence_memory_power32.png"));

    public DiligenceMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.VIRTUE, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(
                new DrawCardAction(this.source, CARDS_DRAWN_ON_ENTER));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(
                    new RetainCardsAction(this.source, CARDS_RETAINED));
        }
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + CARDS_RETAINED + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DiligenceMemory(owner, isClarified);
    }
}
