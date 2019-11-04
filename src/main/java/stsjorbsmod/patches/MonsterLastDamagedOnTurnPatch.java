package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class MonsterLastDamagedOnTurnPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class AbstractMonster_damage
    {
        @SpirePostfixPatch
        public static void patch(AbstractMonster __instance)
        {
            if (__instance.lastDamageTaken > 0) {
                MonsterLastDamagedOnTurnField.lastDamagedOnTurn.set(__instance, AbstractDungeon.actionManager.turn);
            }
        }
    }
}