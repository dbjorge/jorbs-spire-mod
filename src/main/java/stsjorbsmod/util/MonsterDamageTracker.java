package stsjorbsmod.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.HashMap;
import java.util.Map;

public class MonsterDamageTracker {
    private static Map<AbstractMonster, Integer> monsterIdsToLastDamageTurn = new HashMap<>();
    public static void clear() {
        monsterIdsToLastDamageTurn.clear();
    }
    public static void onMonsterDamaged(AbstractMonster monster) {
        monsterIdsToLastDamageTurn.put(monster, AbstractDungeon.actionManager.turn);
    }
    public static boolean hasMonsterBeenDamagedThisTurn(AbstractMonster monster) {
        return monsterIdsToLastDamageTurn.getOrDefault(monster, -1) == AbstractDungeon.actionManager.turn;
    }
}
