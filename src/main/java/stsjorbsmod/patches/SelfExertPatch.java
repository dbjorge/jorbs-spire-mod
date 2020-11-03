package stsjorbsmod.patches;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.relics.OnExertSubscriber;
import stsjorbsmod.util.ReflectionUtils;

import static stsjorbsmod.patches.ExertedPatch.isExerted;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class SelfExertPatch {
    @SpirePrefixPatch
    public static void patch(UseCardAction __this) {
        AbstractCard targetCard = ReflectionUtils.getPrivateField(__this, UseCardAction.class, "targetCard");

        if (SelfExertField.selfExert.get(targetCard) && !isExerted(targetCard)) {
            // If a card gets added that exerts other cards, this contents of this block should be moved to a helper.
            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(targetCard);

            // I'm setting both masterdeck and combatdeck instances of the card to exerted,
            // in case we want to show text on exerted cards, or want to prevent exerted cards from being returned from Exhaust.
            if (masterCard != null) {
                ExertedField.exerted.set(masterCard, true);
            }
            ExertedField.exerted.set(targetCard, true);
            for (AbstractRelic relic : AbstractDungeon.player.relics) {
                if (relic instanceof OnExertSubscriber) {
                    ((OnExertSubscriber) relic).onExert();
                }
            }
        }
    }
}

