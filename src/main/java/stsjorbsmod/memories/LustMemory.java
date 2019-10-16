package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class LustMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final float ATTACK_BONUS_MODIFIER = 1.5f;
    private static final int ATTACK_BONUS_PERCENTAGE_DESCRIPTION = 50;
    private static final int WEAK_ON_FORGET = 2;

    public static final String POWER_ID = JorbsMod.makeID(LustMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("lust_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("lust_memory_power32.png"));

    public LustMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.SIN, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * ATTACK_BONUS_MODIFIER;
        } else {
            return damage;
        }
    }

    @Override
    public void onForget() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new WeakPower(owner, WEAK_ON_FORGET, false)));
    }

    @Override
    protected void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + ATTACK_BONUS_PERCENTAGE_DESCRIPTION + DESCRIPTIONS[1] + WEAK_ON_FORGET + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new LustMemory(owner, isClarified);
    }
}
