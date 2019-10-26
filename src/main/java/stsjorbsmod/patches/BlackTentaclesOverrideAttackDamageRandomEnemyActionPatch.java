package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.powers.BlackTentaclesPower;

@SpirePatch(
        clz = AttackDamageRandomEnemyAction.class,
        method = "update"
)
public class BlackTentaclesOverrideAttackDamageRandomEnemyActionPatch {
    private static final Logger logger = LogManager.getLogger(BlackTentaclesOverrideAttackDamageRandomEnemyActionPatch.class.getName());

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void patch(AttackDamageRandomEnemyAction action) {
        logger.info("Entering AttackDamageRandomEnemyAction patch");
        BlackTentaclesPower.applyPossibleActionTargetOverride(action);
        logger.info("Leaving AttackDamageRandomEnemyAction patch");
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "calculateCardDamage");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
