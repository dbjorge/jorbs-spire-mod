package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.util.ReflectionUtils;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class CardReboundPatch {
    @SpirePrefixPatch
    public static void patch(UseCardAction __this) {
        AbstractCard targetCard = ReflectionUtils.getPrivateField(__this, UseCardAction.class, "targetCard");
        if (CardReboundField.reboundOnce.get(targetCard)) {
            CardReboundField.reboundOnce.set(targetCard, false);
            __this.reboundCard = true;
        }
    }
}
