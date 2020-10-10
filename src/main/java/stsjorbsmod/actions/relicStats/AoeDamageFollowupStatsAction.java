package stsjorbsmod.actions.relicStats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Adapted from RelicStats mod to prevent dependency on RelicStats mod.
 * https://github.com/ForgottenArbiter/StsRelicStats/blob/64b1d26453bbd2738216c6b82b2f419c6396147c/src/main/java/relicstats/actions/AoeDamageFollowupAction.java
 */
public class AoeDamageFollowupStatsAction extends AbstractGameAction {
    Consumer<int[]> statTracker;
    PreAoeDamageStatsAction preAoeDamageStatsAction;

    public AoeDamageFollowupStatsAction(Consumer<int[]> statTracker, PreAoeDamageStatsAction preAoeDamageStatsAction) {
        this.statTracker = statTracker;
        this.preAoeDamageStatsAction = preAoeDamageStatsAction;
        this.actionType = ActionType.DAMAGE;  // So it's not cleared if the damage kills
    }

    @Override
    public void update() {
        statTracker.accept(preAoeDamageStatsAction.getAffectedMonsters().stream().mapToInt(m -> m.lastDamageTaken).toArray());
        isDone = true;
    }
}
