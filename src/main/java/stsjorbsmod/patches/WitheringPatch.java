package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import javassist.CtBehavior;
import stsjorbsmod.powers.CoupDeGracePower;
import stsjorbsmod.powers.WitheringPower;

public class WitheringPatch {
    @SpirePatch(clz = IntangiblePlayerPower.class, method = "atDamageFinalReceive")
    public static class ReduceIntangiblePlayerDamageReductionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn patch(IntangiblePlayerPower __instance, float dmg, DamageInfo.DamageType type) {
            AbstractPower maybeWithering = __instance.owner.getPower(WitheringPower.POWER_ID);
            AbstractPower maybeCoupDeGrace = __instance.owner.getPower(CoupDeGracePower.POWER_ID);
            // damage gets handled in coup de grace patch, but needs to be calculated first
            if(maybeWithering != null && maybeCoupDeGrace == null)
            {
                return SpireReturn.Return(Math.min(dmg, maybeWithering.amount + 1));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = IntangiblePower.class, method = "atDamageFinalReceive")
    public static class ReduceIntangibleDamageReductionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn patch(IntangiblePower __instance, float dmg, DamageInfo.DamageType type) {
            AbstractPower maybeWithering = AbstractDungeon.player.getPower(WitheringPower.POWER_ID);
            AbstractPower maybeCoupDeGrace = AbstractDungeon.player.getPower(CoupDeGracePower.POWER_ID);
            // damage gets handled in coup de grace patch, but needs to be calculated first
            if(maybeWithering != null && maybeCoupDeGrace == null)
            {
                return SpireReturn.Return(Math.min(dmg, maybeWithering.amount + 1));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class ReduceIntangibleDamageReductionInPlayerDamageMethod
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void patch(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
            AbstractPower maybeWithering = __instance.getPower(WitheringPower.POWER_ID);
            if (__instance.hasPower(IntangiblePlayerPower.POWER_ID)
                    && maybeWithering != null
                    && !TrueDamagePatch.TrueDamageInfoField.isTrueDamage.get(info))
            {
                damageAmount[0] = Math.min(maybeWithering.amount + 1, info.output);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return found;
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class ReduceIntangibleDamageReductionInMonsterDamageMethod
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void patch(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            AbstractPower maybeWithering = AbstractDungeon.player.getPower(WitheringPower.POWER_ID);
            if ((__instance.hasPower(IntangiblePlayerPower.POWER_ID) || __instance.hasPower(IntangiblePower.POWER_ID))
                    && maybeWithering != null
                    && !TrueDamagePatch.TrueDamageInfoField.isTrueDamage.get(info))
            {
                damageAmount[0] = Math.min(maybeWithering.amount + 1, info.output);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return found;
            }
        }
    }
}
