package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.powers.*;
import stsjorbsmod.powers.BanishedPower;

@SpirePatch(clz = AngerPower.class, method = "onUseCard")
@SpirePatch(clz = CuriosityPower.class, method = "onUseCard")
@SpirePatch(clz = TimeWarpPower.class, method = "onAfterUseCard")
@SpirePatch(clz = ExplosivePower.class, method = "duringTurn")
@SpirePatch(clz = GenericStrengthUpPower.class, method = "atEndOfRound")
public class BanishPatch {
    @SpirePrefixPatch
    public static SpireReturn prefix(AbstractPower __this) {
        if (__this.owner.hasPower(BanishedPower.POWER_ID)) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}




