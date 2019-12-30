package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnEnergyRechargeToConserveSubscriber;

public class OnEnergyRechargeToConserveSubscriberPatch {
    public static boolean handleAsConserve(EnergyManager energyManager) {
        boolean conserve = false;
        MemoryManager mm = MemoryManager.forPlayer();
        for (AbstractMemory m : mm.allMemoriesIncludingInactive()) {
            if (m instanceof OnEnergyRechargeToConserveSubscriber) {
                boolean subscriberWantsToConserve = ((OnEnergyRechargeToConserveSubscriber)m).onEnergyRechargeToConserve();
                conserve = subscriberWantsToConserve || conserve;
            }
        }
        if (conserve) {
            EnergyPanel.addEnergy(energyManager.energy);
            return true;
        }

        return false;
    }

    @SpirePatch(clz = EnergyManager.class, method = "recharge")
    public static class EnergyManager_recharge {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(EnergyPanel.class.getName()) && methodCall.getMethodName().equals("setEnergy")) {
                        methodCall.replace(String.format("{ if (! %1$s.handleAsConserve(this)) { $_ = $proceed($$); } }", OnEnergyRechargeToConserveSubscriberPatch.class.getName()));
                    }
                }
            };
        }
    }
}