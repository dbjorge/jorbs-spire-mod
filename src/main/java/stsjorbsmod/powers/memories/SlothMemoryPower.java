package stsjorbsmod.powers.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class SlothMemoryPower extends AbstractMemoryPower implements CloneablePowerInterface {
    private static final int DISCARD_ON_REMEMBER = 3;
    private static final int DRAW_REDUCTION = 1;

    public static final String POWER_ID = JorbsMod.makeID(SlothMemoryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("sloth_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("sloth_memory_power32.png"));

    public SlothMemoryPower(final AbstractCreature owner, final AbstractCreature source, boolean isClarified) {
        super(NAME, MemoryType.SIN, owner, source, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        // addToTop for correct interaction with Unseen Servant
        AbstractDungeon.actionManager.addToTop(
                new DiscardAction(owner, source, DISCARD_ON_REMEMBER, true));

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new DrawReductionPower(owner, DRAW_REDUCTION), DRAW_REDUCTION));
    }

    @Override
    public void onRemove() {
        if (this.isClarified) {
            return;
        }

        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, source, DrawReductionPower.POWER_ID, DRAW_REDUCTION));
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + DRAW_REDUCTION + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SlothMemoryPower(owner, source, isClarified);
    }
}
