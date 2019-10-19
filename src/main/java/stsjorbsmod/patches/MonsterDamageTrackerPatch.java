package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.util.MonsterDamageTracker;

public class MonsterDamageTrackerPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class AbstractMonster_damage
    {
        @SpirePostfixPatch
        public static void patch(AbstractMonster __instance)
        {
            MonsterDamageTracker.onMonsterDamaged(__instance);
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class GameActionManager_clear
    {
        @SpirePostfixPatch
        public static void patch(GameActionManager __instance)
        {
            MonsterDamageTracker.clear();
        }
    }
}