package stsjorbsmod.powers.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class GreedMemoryPower extends AbstractMemoryPower implements CloneablePowerInterface {
    private static final int GOLD_PER_KILL = 10;

    public static final String POWER_ID = JorbsMod.makeID(GreedMemoryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("greed_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("greed_memory_power32.png"));

    public GreedMemoryPower(final AbstractCreature owner, final AbstractCreature source, boolean isClarified) {
        super(NAME, MemoryType.SIN, owner, source, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAttack(DamageInfo damageInfo, int damage, AbstractCreature target) {
        if (target.isPlayer || target.isDead || target.isDying || target.halfDead || target.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        if (damage >= target.currentHealth) {
            JorbsMod.logger.info("Greed: gaining gold");
            AbstractDungeon.player.gainGold(GOLD_PER_KILL);
        }
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + GOLD_PER_KILL + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GreedMemoryPower(owner, source, isClarified);
    }
}
