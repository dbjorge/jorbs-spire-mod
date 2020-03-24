package stsjorbsmod.actions;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.CustomJorbsModPower;

import java.util.Arrays;
import java.util.function.Consumer;

public class TimeEddyAction extends AbstractTimeProgressionAction {
    private static Runnable[] BUFF_DEBUFF_TIME_PROGRESSION = new Runnable[] {
            () -> forEachApplicablePlayerMemory(m -> m.atEndOfTurnPreEndTurnCards()),
            () -> forEachApplicablePlayerMemory(m -> m.atEndOfTurn(true)),
            () -> forEachApplicablePlayerPower(p -> p.atEndOfTurnPreEndTurnCards(true)),
            () -> forEachApplicablePlayerPower(p -> p.atEndOfTurn(true)),
        
            // Monster turn start
            () -> forEachApplicableMonsterPower(p -> { if (p instanceof CustomJorbsModPower) { ((CustomJorbsModPower) p).atStartOfTurnPreLoseBlock(); } }),
            () -> forEachApplicableMonsterPower(p -> p.atStartOfTurn()),
            () -> forEachApplicableMonsterPower(p -> p.atStartOfTurnPostDraw()),
        
            // Monster turn
            () -> forEachApplicableMonsterPower(p -> p.duringTurn()),
        
            // Monster turn end
            () -> forEachApplicableMonsterPower(p -> p.atEndOfTurnPreEndTurnCards(false)),
            () -> forEachApplicableMonsterPower(p -> p.atEndOfTurn(false)),
        
            // Round end
            () -> forEachApplicablePlayerPower(p -> p.atEndOfRound()),
            () -> forEachApplicableMonsterPower(p -> p.atEndOfRound()),
        
            // Player turn start
            () -> forEachApplicablePlayerPower(p -> p.atStartOfTurn()),
            () -> forEachApplicablePlayerMemory(m -> m.atStartOfTurnPostDraw()),
            () -> forEachApplicablePlayerPower(p -> p.atStartOfTurnPostDraw()),
        
            // Player turn
            () -> forEachApplicablePlayerPower(p -> p.duringTurn()),
    };
    
    public TimeEddyAction(int turnsToAdvance) {
        super(Arrays.asList(BUFF_DEBUFF_TIME_PROGRESSION), turnsToAdvance);
    }

    private static boolean shouldAffectPower(AbstractPower power) {
        return power.type == AbstractPower.PowerType.BUFF || power.type == AbstractPower.PowerType.DEBUFF;
    }

    private static void forEachApplicablePlayerPower(Consumer<AbstractPower> callback) {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (shouldAffectPower(power)) {
                callback.accept(power);
            }
        }
    }

    private static void forEachApplicableMonsterPower(Consumer<AbstractPower> callback) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                for (AbstractPower power : m.powers) {
                    if (shouldAffectPower(power)) {
                        callback.accept(power);
                    }
                }
            }
        }
    }

    private static void forEachApplicablePlayerMemory(Consumer<AbstractMemory> callback) {
        MemoryManager mm = MemoryManager.forPlayer(AbstractDungeon.player);
        if (mm != null) {
            for (AbstractMemory memory : mm.currentMemories()) {
                callback.accept(memory);
            }
        }
    }
}
