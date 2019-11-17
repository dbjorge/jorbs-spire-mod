package stsjorbsmod.util;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.relics.RunicDome;

import java.util.function.Predicate;

public class IntentUtils {
    public static boolean areIntentsHidden() {
        return AbstractDungeon.player.hasRelic(RunicDome.ID);
    }

    public static boolean isDebuffIntent(Intent intent) {
        return
                intent == Intent.STRONG_DEBUFF ||
                intent == Intent.ATTACK_DEBUFF ||
                intent == Intent.DEBUFF ||
                intent == Intent.DEFEND_DEBUFF;
    }

    public static boolean isAttackIntent(Intent intent) {
        return
                intent == Intent.ATTACK ||
                intent == Intent.ATTACK_BUFF ||
                intent == Intent.ATTACK_DEBUFF ||
                intent == Intent.ATTACK_DEFEND;
    }

    public static boolean playerCanSeeThatAnyEnemyIntentMatches(Predicate<Intent> intentPredicate) {
        return !areIntentsHidden() && anyEnemyIntentMatches(intentPredicate);
    }

    public static boolean anyEnemyIntentMatches(Predicate<Intent> intentPredicate) {
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(
                m -> !m.isDeadOrEscaped() && intentPredicate.test(m.intent));
    }
}
