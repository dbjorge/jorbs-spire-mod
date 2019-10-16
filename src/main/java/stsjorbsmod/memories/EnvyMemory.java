package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class EnvyMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int VULNERABLE_ON_REMEMBER = 1;
    private static final int VULNERABLE_ON_TARGET_ENEMY = 1;

    public static final String POWER_ID = JorbsMod.makeID(EnvyMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("envy_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("envy_memory_power32.png"));

    public EnvyMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.SIN, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new VulnerablePower(owner, VULNERABLE_ON_REMEMBER, false), VULNERABLE_ON_REMEMBER));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(monster, owner, new VulnerablePower(monster, VULNERABLE_ON_TARGET_ENEMY, false), VULNERABLE_ON_TARGET_ENEMY));
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + VULNERABLE_ON_TARGET_ENEMY + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new EnvyMemory(owner, isClarified);
    }
}
