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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class KindnessMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int POISON_ON_REMEMBER = 3;
    private static final int ENVENOM_MAGNITUDE = 1;

    public static final String POWER_ID = JorbsMod.makeID(KindnessMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("kindness_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("kindness_memory_power32.png"));

    public KindnessMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.VIRTUE, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onRemember() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for(AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.isDead && !monster.isDying) {
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(monster, owner, new PoisonPower(monster, owner, POISON_ON_REMEMBER), POISON_ON_REMEMBER));
                }
            }
        }

        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new EnvenomPower(owner, ENVENOM_MAGNITUDE), ENVENOM_MAGNITUDE));
    }

    @Override
    public void onForget() {
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, source, EnvenomPower.POWER_ID, ENVENOM_MAGNITUDE));
    }

    @Override
    protected void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + ENVENOM_MAGNITUDE + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new KindnessMemory(owner, isClarified);
    }
}
