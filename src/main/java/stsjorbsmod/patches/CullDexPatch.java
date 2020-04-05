package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.util.ReflectionUtils;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class CullDexPatch {
    @SpirePrefixPatch
    public static void Prefix(ApplyPowerAction __this) {
        float duration = ReflectionUtils.getPrivateField(__this, AbstractGameAction.class, "duration");
        float startingDuration = ReflectionUtils.getPrivateField(__this, ApplyPowerAction.class, "startingDuration");
        if (duration == startingDuration) {
            AbstractPower originalPower = ReflectionUtils.getPrivateField(__this, ApplyPowerAction.class, "powerToApply");
            AbstractPower updatedPower = updateDexPower(originalPower);
            if (originalPower != updatedPower) {
                ReflectionUtils.setPrivateField(__this, ApplyPowerAction.class, "powerToApply", updatedPower);
            }
        }
    }

    public static AbstractPower updateDexPower(AbstractPower power) {
        if (AbstractDungeon.player instanceof Cull && power.owner instanceof Cull) {
            if(power instanceof DexterityPower) {
                return new StrengthPower(power.owner, power.amount);
            } else if (power instanceof LoseDexterityPower) {
                return new LoseStrengthPower(power.owner, power.amount);
            }
        }
        return power;
    }
}


