package stsjorbsmod.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;

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

    public static AbstractMonster getRandomMonster(MonsterGroup group, List<AbstractMonster> exceptions, boolean aliveOnly, Random rng) {
        if (group.areMonstersBasicallyDead()) {
            return null;
        } else {
            ArrayList<AbstractMonster> tmp;
            if (group.monsters.size() == 1) {
                return group.monsters.get(0);
            } else if (exceptions == null || exceptions.isEmpty()) {
                if (aliveOnly) {
                    tmp = new ArrayList<>();

                    for (AbstractMonster m : group.monsters) {
                        if (!m.halfDead && !m.isDying && !m.isEscaping) {
                            tmp.add(m);
                        }
                    }

                    if (tmp.size() <= 0) {
                        return null;
                    } else {
                        return tmp.get(rng.random(0, tmp.size() - 1));
                    }
                } else {
                    return group.monsters.get(rng.random(0, group.monsters.size() - 1));
                }
            } else if (exceptions.containsAll(group.monsters)) {
                return null;
            } else if (aliveOnly) {
                tmp = new ArrayList<>();
                for (AbstractMonster m : group.monsters) {
                    if (!m.halfDead && !m.isDying && !m.isEscaping && !exceptions.contains(m)) {
                        tmp.add(m);
                    }
                }

                if (tmp.size() == 0) {
                    return null;
                } else {
                    return tmp.get(rng.random(0, tmp.size() - 1));
                }
            } else {
                tmp = new ArrayList<>();
                for (AbstractMonster m : group.monsters) {
                    if (!exceptions.contains(m)) {
                        tmp.add(m);
                    }
                }

                return tmp.get(rng.random(0, tmp.size() - 1));
            }
        }
    }
}
