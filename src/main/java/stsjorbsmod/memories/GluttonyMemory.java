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

public class GluttonyMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(GluttonyMemory.class);

    private static final int MAX_HP_PER_KILL = 3;

    public GluttonyMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
    }

    @Override
    public void onAttack(DamageInfo damageInfo, int damage, AbstractCreature target) {
        if (target.isPlayer || target.isDead || target.isDying || target.halfDead || target.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        if (damage >= target.currentHealth) {
            JorbsMod.logger.info("Gluttony: gaining max hp");
            AbstractDungeon.player.increaseMaxHp(MAX_HP_PER_KILL, true);
        }
    }

    @Override
    protected void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + MAX_HP_PER_KILL + DESCRIPTIONS[1];
    }
}
