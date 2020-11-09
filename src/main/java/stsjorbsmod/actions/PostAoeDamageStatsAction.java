package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.relics.CustomJorbsModIntStatsRelic;

import java.util.ArrayList;

// Adapted from Relic Stats' AoeDamageFollowupAction (https://github.com/ForgottenArbiter/StsRelicStats)

public class PostAoeDamageStatsAction extends AbstractGameAction {

    private PreAoeDamageStatsAction preAction;
    private CustomJorbsModIntStatsRelic statTracker;

    public PostAoeDamageStatsAction(CustomJorbsModIntStatsRelic statTracker, PreAoeDamageStatsAction preAction) {
        this.statTracker = statTracker;
        this.preAction = preAction;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;  // So it's not cleared if the damage kills
    }

    @Override
    public void update() {
        ArrayList<AbstractMonster> affectedMonsters = preAction.getAffectedMonsters();
        for (AbstractMonster m : affectedMonsters) {
            statTracker.addStats(m.lastDamageTaken);
        }
        isDone = true;
    }
}
