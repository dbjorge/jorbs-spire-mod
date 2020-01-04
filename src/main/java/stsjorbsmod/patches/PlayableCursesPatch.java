package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.powers.CatharsisPower;

public class PlayableCursesPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "canUse"
    )
    public static class AbstractCard_canUse
    {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> patch(AbstractCard __this)
        {
            if (__this.type == AbstractCard.CardType.CURSE && AbstractDungeon.player.hasPower(CatharsisPower.POWER_ID)) {
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }
}