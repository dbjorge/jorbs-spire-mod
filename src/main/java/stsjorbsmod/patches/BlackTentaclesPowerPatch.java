package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import stsjorbsmod.powers.BlackTentaclesPower;

public class BlackTentaclesPowerPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class AbstractMonster_damage
        {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "damageAmount" }
        )
        public static void patch(AbstractMonster __this, DamageInfo info, @ByRef int[] damageAmount)
        {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower power : m.powers) {
                    if (power instanceof BlackTentaclesPower) {
                        damageAmount[0] = ((BlackTentaclesPower) power).onAnyMonsterHpLoss(__this, info, damageAmount[0]);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}