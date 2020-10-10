package stsjorbsmod.actions.relicStats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

/**
 * Adapted from RelicStats mod to prevent dependency on RelicStats mod.
 * https://github.com/ForgottenArbiter/StsRelicStats/blob/64b1d26453bbd2738216c6b82b2f419c6396147c/src/main/java/relicstats/actions/PreAoeDamageAction.java
 */
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
