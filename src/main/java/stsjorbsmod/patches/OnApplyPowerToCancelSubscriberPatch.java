package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import stsjorbsmod.powers.IOnApplyPowerToCancelSubscriber;

public class OnApplyPowerToCancelSubscriberPatch {
    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPowerAction_update
    {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn patch(ApplyPowerAction __this)
        {
            AbstractCreature target = __this.target;
            AbstractCreature source = __this.source;
            for (AbstractPower power : target.powers) {
                if (power instanceof IOnApplyPowerToCancelSubscriber) {
                    boolean shouldCancel = ((IOnApplyPowerToCancelSubscriber) power).onReceivePowerToCancel(power, source);
                    if (shouldCancel) {
                        __this.isDone = true;
                        return SpireReturn.Return(null);
                    }
                }
            }
            if (source != null) {
                for (AbstractPower power : source.powers) {
                    if (power instanceof IOnApplyPowerToCancelSubscriber) {
                        boolean shouldCancel = ((IOnApplyPowerToCancelSubscriber) power).onGivePowerToCancel(power, target);
                        if (shouldCancel) {
                            __this.isDone = true;
                            return SpireReturn.Return(null);
                        }
                    }
                }
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(ApplyPowerAction.class, "source");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}