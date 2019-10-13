package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.powers.BlackTentaclesPower;

import java.util.ArrayList;

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
        ArrayList<AbstractCreature> candidateTargetsWithBlackTentacles = new ArrayList<>();
        for (AbstractCreature m : AbstractDungeon.getMonsters().monsters) {
            if (!m.halfDead && !m.isDying && !m.isEscaping && m.hasPower(BlackTentaclesPower.POWER_ID)) {
                candidateTargetsWithBlackTentacles.add(m);
            }
        }
        if (!candidateTargetsWithBlackTentacles.isEmpty()) {
            logger.info("Black Tentacles overriding AttackDamageRandomEnemyAction target");
            int blackTentaclesTargetIndex = AbstractDungeon.cardRandomRng.random(0, candidateTargetsWithBlackTentacles.size() - 1);
            action.target = candidateTargetsWithBlackTentacles.get(blackTentaclesTargetIndex);
        }
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
