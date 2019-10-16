package stsjorbsmod.memories;

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

public class GreedMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(GreedMemory.class);

    private static final int GOLD_PER_KILL = 10;

    public GreedMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
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
    protected void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + GOLD_PER_KILL + DESCRIPTIONS[1];
    }
}
