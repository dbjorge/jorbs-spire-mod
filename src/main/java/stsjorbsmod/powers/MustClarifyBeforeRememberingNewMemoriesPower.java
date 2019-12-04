package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class MustClarifyBeforeRememberingNewMemoriesPower extends AbstractPower implements OnModifyMemoriesSubscriber {
    public static final String POWER_ID = JorbsMod.makeID(MustClarifyBeforeRememberingNewMemoriesPower.class);
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("MustClarifyBeforeRememberingNewMemoriesPower_84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("MustClarifyBeforeRememberingNewMemoriesPower_32.png"));

    private final String memoryIDThatMustBeClarified;

    public MustClarifyBeforeRememberingNewMemoriesPower(AbstractCreature owner, String memoryIDThatMustBeClarified) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;

        this.memoryIDThatMustBeClarified = memoryIDThatMustBeClarified;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public boolean onRememberMemoryToCancel(String memoryIDBeingRemembered) {
        this.flash();
        return !memoryIDBeingRemembered.equals(memoryIDThatMustBeClarified);
    }

    @Override
    public void onGainClarity(String id) {
        if (id.equals(memoryIDThatMustBeClarified)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    @Override
    public void onSnap() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
