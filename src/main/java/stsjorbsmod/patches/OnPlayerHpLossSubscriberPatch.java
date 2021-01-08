package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import stsjorbsmod.cards.OnPlayerHpLossCardSubscriber;
import stsjorbsmod.cards.WasHPLostCardSubscriber;
import stsjorbsmod.powers.OnPlayerHpLossPowerSubscriber;

import java.util.Collections;

public class OnPlayerHpLossSubscriberPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class AbstractPlayer_damage {
        @SpireInsertPatch(
                locator = lastDamageTakenLocator.class,
                localvars = {"damageAmount"}
        )
        public static void patch(AbstractPlayer __this, DamageInfo info, @ByRef int[] damageAmount) {
            // We order the power first somewhat arbitrarily, because we want Strange Pendant to happen before Mage Armor
            for (AbstractPower power : __this.powers) {
                if (power instanceof OnPlayerHpLossPowerSubscriber) {
                    damageAmount[0] = ((OnPlayerHpLossPowerSubscriber) power).onPlayerHpLoss(damageAmount[0]);
                }
            }

            for (AbstractCard cardInHand : __this.hand.group) {
                if (cardInHand instanceof OnPlayerHpLossCardSubscriber) {
                    damageAmount[0] = ((OnPlayerHpLossCardSubscriber) cardInHand).onPlayerHpLossWhileInHand(damageAmount[0]);
                }
            }
        }

        private static class lastDamageTakenLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "lastDamageTaken");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpireInsertPatch(
                locator = PostRelicWasHPLostLocator.class,
                localvars = {"damageAmount"}
        )
        public static void wasHPLostPatch(AbstractPlayer __this, DamageInfo info, int damageAmount) {
            for (AbstractCard cardInHand : __this.hand.group) {
                if (cardInHand instanceof WasHPLostCardSubscriber) {
                    ((WasHPLostCardSubscriber) cardInHand).wasHPLost(damageAmount);
                }
            }
        }

        private static class PostRelicWasHPLostLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher relicWasHPLostMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "wasHPLost");
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(DamageInfo.class, "owner");
                return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(relicWasHPLostMatcher), finalMatcher);
            }
        }
    }
}