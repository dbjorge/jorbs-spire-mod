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
import com.megacrit.cardcrawl.powers.ThornsPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class HumilityMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int THORNS_DAMAGE = 2;

    public static final String POWER_ID = JorbsMod.makeID(HumilityMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("humility_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("humility_memory_power32.png"));

    public HumilityMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.VIRTUE, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new ThornsPower(owner, THORNS_DAMAGE)));
    }

    @Override
    public void onRemove() {
        if (isClarified) {
            return;
        }

        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, source, ThornsPower.POWER_ID, THORNS_DAMAGE));
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + THORNS_DAMAGE + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new HumilityMemory(owner, isClarified);
    }
}
