package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class ChastityMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int DEXTERITY_ON_REMEMBER = 2;
    private static final int DEXTERITY_LOSS_PER_TURN = 1;
    private static final int BLOCK_PER_TURN = 6;

    public static final String POWER_ID = JorbsMod.makeID(ChastityMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("chastity_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("chastity_memory_power32.png"));

    public ChastityMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.VIRTUE, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new DexterityPower(owner, DEXTERITY_ON_REMEMBER), DEXTERITY_ON_REMEMBER));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }

        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, source, DexterityPower.POWER_ID, DEXTERITY_LOSS_PER_TURN));
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(owner, source, BLOCK_PER_TURN));
    }

    @Override
    protected void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + DEXTERITY_LOSS_PER_TURN + DESCRIPTIONS[1] + BLOCK_PER_TURN + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChastityMemory(owner, isClarified);
    }
}
