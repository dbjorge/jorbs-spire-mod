package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;
import stsjorbsmod.cards.OnPlayerHpLossSubscriber;

public class OnPlayerHpLossSubscriberPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class AbstractPlayer_damage
    {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "damageAmount" }
        )
        public static void patch(AbstractPlayer __this, DamageInfo info, @ByRef int[] damageAmount)
        {
            for (AbstractCard cardInHand : __this.hand.group) {
                if (cardInHand instanceof OnPlayerHpLossSubscriber) {
                    damageAmount[0] = ((OnPlayerHpLossSubscriber) cardInHand).onPlayerHpLossWhileInHand(damageAmount[0]);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}