package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

// Adapted from Relic Stats' PreAoeDamageAction (https://github.com/ForgottenArbiter/StsRelicStats)

public class PreAoeDamageStatsAction extends AbstractGameAction {

    private ArrayList<AbstractMonster> affectedMonsters;

    public PreAoeDamageStatsAction() {
        this.affectedMonsters = new ArrayList<>();
    }

    public void update() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!(m.isDead || m.isDeadOrEscaped() || m.currentHealth <= 0)) {
                this.affectedMonsters.add(m);
                m.lastDamageTaken = 0;
            }
        }
        this.isDone = true;
    }

    public ArrayList<AbstractMonster> getAffectedMonsters() {
        return this.affectedMonsters;
    }

}
