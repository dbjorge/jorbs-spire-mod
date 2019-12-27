package stsjorbsmod.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;

public class CombatUtils {
    /**
     * If there are any non-minion monsters still alive, then this returns false.
     * @return returns true if there are no non-minion monsters left alive. Otherwise, returns false.
     */
    public static boolean isCombatBasicallyVictory() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.hasPower(MinionPower.POWER_ID) && !(m.isDying || m.isDead)) {
                return false;
            }
        }
        return true;
    }
}
