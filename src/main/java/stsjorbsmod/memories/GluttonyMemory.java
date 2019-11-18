package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;

public class GluttonyMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(GluttonyMemory.class);

    private static final int MAX_HP_PER_KILL = 2;
    private static final int DISCARD_AT_START_OF_TURN = 1;

    public GluttonyMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.SIN, owner);
        setDescriptionPlaceholder("!M!", MAX_HP_PER_KILL);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (isPassiveEffectActive() && !m.hasPower(MinionPower.POWER_ID)) {
            JorbsMod.logger.info("Gluttony: gaining max hp");
            AbstractDungeon.player.increaseMaxHp(MAX_HP_PER_KILL, true);
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (isPassiveEffectActive()) {
            AbstractDungeon.actionManager.addToBottom(new DiscardAction(owner, owner, DISCARD_AT_START_OF_TURN, false));
        }
    }
}
