package stsjorbsmod.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static AbstractMonster getRandomAliveMonster(MonsterGroup group, Predicate<AbstractMonster> isCandidate, Random rng) {
        return getRandomMonster(group, m -> (!m.halfDead && !m.isDying && !m.isEscaping && isCandidate.test(m)), rng);
    }

    public static AbstractMonster getRandomMonster(MonsterGroup group, Predicate<AbstractMonster> isCandidate, Random rng) {
        List<AbstractMonster> candidates = group.monsters.stream().filter(isCandidate).collect(Collectors.toList());
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(rng.random(0, candidates.size() - 1));
    }
}
