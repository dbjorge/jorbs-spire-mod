package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import stsjorbsmod.powers.StackableTwoAmountPower;

public class StackableTwoAmountPowerPatch {

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class UpdateHook {
        @SpireInsertPatch(
                locator = StackableTwoAmountPowerPatch.UpdateHook.Locator.class,
                localvars = {"p", "this.powerToApply"}
        )
        public static SpireReturn patch(ApplyPowerAction __this, AbstractPower p, AbstractPower powerToApply) {
            if (p instanceof StackableTwoAmountPower) {
                ((StackableTwoAmountPower) p).stackSecondPower(((StackableTwoAmountPower) powerToApply).amount2);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher abstractPowerStackPower = new Matcher.MethodCallMatcher(AbstractPower.class, "stackPower");
                return LineFinder.findInOrder(ctMethodToPatch, abstractPowerStackPower);
            }
        }
    }
}
