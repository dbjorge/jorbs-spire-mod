package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import stsjorbsmod.powers.BanishedPower;

public class BanishPatch {
    @SpirePatch(
            clz = TimeWarpPower.class,
            method = "onAfterUseCard"
    )
    public static class TimeWarpPower_onAfterUseCard {
        @SpirePrefixPatch
        public static SpireReturn prefix(TimeWarpPower __this, AbstractCard card, UseCardAction action) {
            if (__this.owner.hasPower(BanishedPower.POWER_ID)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}




