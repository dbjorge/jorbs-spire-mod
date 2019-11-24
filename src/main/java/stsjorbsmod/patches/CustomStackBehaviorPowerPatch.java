package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.powers.CustomStackBehaviorPower;

public class CustomStackBehaviorPowerPatch {
    public static boolean patchedStackPower(AbstractPower originalPower, AbstractPower newPower) {
        if (originalPower instanceof CustomStackBehaviorPower) {
            ((CustomStackBehaviorPower) originalPower).stackPower(newPower);
            return false;
        } else {
            return true;
        }
    }

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ApplyPowerAction_update {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                if (methodCall.getClassName().equals(AbstractPower.class.getName()) && methodCall.getMethodName().equals("stackPower")) {
                    methodCall.replace("{ if (" + CustomStackBehaviorPowerPatch.class.getName() + ".patchedStackPower($0, this.powerToApply)) { $_ = $proceed($$); } }");
                }
                }
            };
        }
    }
}
