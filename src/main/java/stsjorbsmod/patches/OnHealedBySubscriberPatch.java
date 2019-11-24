package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.powers.OnHealedBySubscriber;

public class OnHealedBySubscriberPatch {
    public static void applyAllHealedBySubscribers(HealAction action) {
        for (AbstractPower power : action.target.powers) {
            if (power instanceof OnHealedBySubscriber) {
                action.amount = ((OnHealedBySubscriber) power).onHealedBy(action.source, action.amount);
            }
        }
    }

    @SpirePatch(
            clz = HealAction.class,
            method = "update"
    )
    public static class HealAction_update {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractCreature.class.getName()) && methodCall.getMethodName().equals("heal")) {
                        methodCall.replace(String.format("{ %1$s.applyAllHealedBySubscribers(this); if (this.amount > 0) { $_ = $proceed($$); } }", OnHealedBySubscriberPatch.class.getName()));
                    }
                }
            };
        }
    }
}