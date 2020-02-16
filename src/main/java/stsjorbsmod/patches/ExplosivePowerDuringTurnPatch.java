package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.powers.ExplosivePower;

@SpirePatch(clz = ExplosivePower.class, method = "duringTurn")
public class ExplosivePowerDuringTurnPatch {
    @SpirePrefixPatch
    public static SpireReturn prefix(ExplosivePower __this) {
        if (__this.owner.isDeadOrEscaped()) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
