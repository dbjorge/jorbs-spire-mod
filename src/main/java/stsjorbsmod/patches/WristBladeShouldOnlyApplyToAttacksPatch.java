package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.WristBlade;

// This patch fixes an issue where Wrist Blade is supposed to only apply to attacks, but actually applies to any
// card which deals direct damage. This causes a mistaken interaction with Wanderer's Snake Oil.
public class WristBladeShouldOnlyApplyToAttacksPatch {
    @SpirePatch(clz = WristBlade.class, method = "atDamageModify")
    public static class WristBlade_atDamageModifyPatch {
        @SpirePrefixPatch
        public static SpireReturn<Float> prefix(WristBlade __this, float damage, AbstractCard c) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                return SpireReturn.Return(damage);
            }
            return SpireReturn.Continue();
        }
    }
}
