package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.*;
import stsjorbsmod.powers.BanishedPower;

public class BanishPatch {
    public static SpireReturn returnEarlyIfBanished(AbstractCreature c) {
        if (c.hasPower(BanishedPower.POWER_ID)) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    @SpirePatch(
            clz = TimeWarpPower.class,
            method = "onAfterUseCard"
    )
    public static class TimeWarpPower_onAfterUseCard {
        @SpirePrefixPatch
        public static SpireReturn prefix(TimeWarpPower __this, AbstractCard card, UseCardAction action) {
            return returnEarlyIfBanished(__this.owner);
        }
    }

    @SpirePatch(clz = GenericStrengthUpPower.class, method = "atEndOfRound")
    public static class GenericStrengthUpPower_AtEndOfRound {
        @SpirePrefixPatch
        public static SpireReturn prefix(GenericStrengthUpPower __this) {
            return returnEarlyIfBanished(__this.owner);
        }
    }

    @SpirePatch(clz = AngerPower.class, method = "onUseCard")
    @SpirePatch(clz = CuriosityPower.class, method = "onUseCard")
    public static class Power_onUseCard {
        @SpirePrefixPatch
        public static SpireReturn prefix(AbstractPower __this, AbstractCard card, UseCardAction action) {
            return returnEarlyIfBanished(__this.owner);
        }
    }
}




