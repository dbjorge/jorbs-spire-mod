package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import stsjorbsmod.powers.TwoAmountPower;

public class StackTwoAmountPowerPatch {

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class UpdateHook {
        @SpireInsertPatch(
                locator = StackTwoAmountPowerPatch.UpdateHook.Locator.class,
                localvars = {"p", "this.powerToApply"}
        )
        public static SpireReturn patch(ApplyPowerAction __this, AbstractPower p, AbstractPower powerToApply) {
            if (p instanceof TwoAmountPower) {
                ((TwoAmountPower) p).stackSecondPower(((TwoAmountPower) powerToApply).amount2);
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
