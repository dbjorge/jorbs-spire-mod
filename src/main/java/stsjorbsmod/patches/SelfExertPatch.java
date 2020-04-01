package stsjorbsmod.patches;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.util.ReflectionUtils;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class SelfExertPatch {
    @SpirePrefixPatch
    public static void patch(UseCardAction __this) {
        AbstractCard targetCard = ReflectionUtils.getPrivateField(__this, UseCardAction.class, "targetCard");

        if (SelfExertField.selfExert.get(targetCard)) {
            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(targetCard);

            // I'm setting both masterdeck and combatdeck instances of the card to exerted,
            // in case we want to show text on exerted cards, or want to prevent exerted cards from being returned from Exhaust.
            if (masterCard != null) {
                ExertedField.exerted.set(masterCard, true);
            }
            ExertedField.exerted.set(targetCard, true);
        }
    }
}

