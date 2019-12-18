package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class TImeEddyAction extends AbstractGameAction {
    public TImeEddyAction(AbstractCreature target, int turnsToAdvance) {
        this.amount = turnsToAdvance;
    }

    private static boolean shouldAffectPower(AbstractPower power) {
        return power.type == AbstractPower.PowerType.BUFF || power.type == AbstractPower.PowerType.DEBUFF;
    }

    private void forEachApplicablePlayerPower(Consumer<AbstractPower> callback) {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (shouldAffectPower(power)) {
                callback.accept(power);
            }
        }
    }

    private void forEachApplicableMonsterPower(Consumer<AbstractPower> callback) {
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

    private void forEachApplicablePlayerMemory(Consumer<AbstractMemory> callback) {
        MemoryManager mm = MemoryManager.forPlayer(target);
        if (mm != null) {
            for (AbstractMemory memory : mm.currentMemories()) {
                callback.accept(memory);
            }
        }
    }

    // TODO: remove these reflection gymnastics once beta branch releases
    // This callback is beta-branch only
    private static final Method AbstractPower_atEndOFTurnPreEndTurnCards = ReflectionUtils.tryGetMethod(AbstractPower.class, "atEndOfTurnPreEndTurnCards", boolean.class);
    private static void atEndOfTurnPreEndTurnCards(AbstractPower p, boolean isPlayer) {
        if (AbstractPower_atEndOFTurnPreEndTurnCards != null) {
            try {
                AbstractPower_atEndOFTurnPreEndTurnCards.invoke(p, isPlayer);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; ++i) {
            // Player turn end
            forEachApplicablePlayerPower(p -> atEndOfTurnPreEndTurnCards(p, true));
            forEachApplicablePlayerMemory(m -> m.atEndOfTurn(true));
            forEachApplicablePlayerPower(p -> p.atEndOfTurn(true));

            // Monster turn start
            forEachApplicableMonsterPower(p -> p.atStartOfTurn());
            forEachApplicableMonsterPower(p -> p.atStartOfTurnPostDraw());

            // Monster turn end
            forEachApplicableMonsterPower(p -> atEndOfTurnPreEndTurnCards(p, false));
            forEachApplicableMonsterPower(p -> p.atEndOfTurn(false));

            // Round end
            forEachApplicablePlayerPower(p -> p.atEndOfRound());
            forEachApplicableMonsterPower(p -> p.atEndOfRound());

            // Player turn start
            forEachApplicablePlayerPower(p -> p.atStartOfTurn());
            forEachApplicablePlayerMemory(m -> m.atStartOfTurnPostDraw());
            forEachApplicablePlayerPower(p -> p.atStartOfTurnPostDraw());
        }

        isDone = true;
    }
}
