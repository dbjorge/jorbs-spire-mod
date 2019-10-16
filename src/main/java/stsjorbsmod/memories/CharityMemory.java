package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

// TODO: It would be nice for this to update if player gold changes mid-combat
public class CharityMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int STRENGTH_PER_GOLD_THRESHOLD = 1;
    private static final int GOLD_THRESHOLD = 100;

    public static final String POWER_ID = JorbsMod.makeID(CharityMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("charity_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("charity_memory_power32.png"));

    public CharityMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.VIRTUE, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    private int strengthAdded;

    private int calculateStrengthToAdd() {
        return (AbstractDungeon.player.gold / GOLD_THRESHOLD) * STRENGTH_PER_GOLD_THRESHOLD;
    }

    @Override
    public void onRemember() {
        this.strengthAdded = calculateStrengthToAdd();
        if (this.strengthAdded > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, source, new StrengthPower(owner, this.strengthAdded), this.strengthAdded));
        }
    }

    @Override
    public void onForget() {
        if (this.strengthAdded > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(owner, source, StrengthPower.POWER_ID, this.strengthAdded));
        }
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + STRENGTH_PER_GOLD_THRESHOLD + DESCRIPTIONS[1] + GOLD_THRESHOLD + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CharityMemory(owner, isClarified);
    }
}
