package stsjorbsmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.Expectation;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import javassist.expr.Expr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;

public class DamageAsBurningPatch {
    public static Logger logger = LogManager.getLogger(DamageAsBurningPatch.class.getName());

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class onAttackHook {
        @SpireInsertPatch(
                locator = DamageAsBurningPatch.onAttackHook.Locator.class,
                localvars = {}
        )
        public static SpireReturn patch(AbstractMonster __this, DamageInfo info) {
            if (info.owner == AbstractDungeon.player) {
                if (info.type == EnumsPatch.BURNING) {
                    logger.info("WE BURNING!");
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher abstractMonsterCurrentHealth = new Matcher.FieldAccessMatcher(AbstractMonster.class, "currentHealth");
                int[] lines = LineFinder.findInOrder(ctMethodToPatch, abstractMonsterCurrentHealth);
                for (int i : lines) {
                    logger.info(i + "");
                }
                return lines;
            }
        }
    }
}
