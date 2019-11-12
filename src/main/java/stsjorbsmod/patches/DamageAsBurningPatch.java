package stsjorbsmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.powers.BurningPower;

public class DamageAsBurningPatch {
    public static Logger logger = LogManager.getLogger(DamageAsBurningPatch.class.getName());

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class onAttackHook {
        @SpireInsertPatch(
                locator = DamageAsBurningPatch.onAttackHook.Locator.class,
                localvars = {"damageAmount"}
        )
        public static SpireReturn patch(AbstractMonster __this, DamageInfo info, int damageAmount) {
            if (info.owner == AbstractDungeon.player && isBurningField.isBurning.get(info) && damageAmount > 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(__this, info.owner, new BurningPower(__this, info.owner, damageAmount)));
                return SpireReturn.Return(null);
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

    /**
     * The purpose of isBurning on {@link DamageInfo} is to indicate to the {@link onAttackHook} to apply {@link BurningPower}
     * based on the damage values available. Unfortunately, adding a new {@link DamageInfo.DamageType} doesn't work since
     * {@link com.megacrit.cardcrawl.powers.CurlUpPower} and {@link com.megacrit.cardcrawl.powers.MalleablePower} check for
     * {@link com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL} to trigger. This patch also allows for no change to
     * {@link stsjorbsmod.memories.LustMemory} which applies burning on hit.
     */
    @SpirePatch(
            clz = DamageInfo.class,
            method = SpirePatch.CLASS
    )
    public static class isBurningField {
        public static SpireField<Boolean> isBurning = new SpireField<>(() -> Boolean.FALSE);
    }
}
