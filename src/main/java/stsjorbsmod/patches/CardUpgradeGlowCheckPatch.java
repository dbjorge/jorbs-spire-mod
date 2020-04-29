package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.util.CombatUtils;

// This is for the benefit of the interaction between Warped Tongs and Mania's glow check
public class CardUpgradeGlowCheckPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "upgradeName"
    )
    public static class AbstractCard_upgradeName
    {
        @SpirePostfixPatch
        public static void patch(AbstractCard __this)
        {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hand != null && CombatUtils.isInCombat())
            {
                AbstractDungeon.player.hand.glowCheck();
            }
        }
    }
}