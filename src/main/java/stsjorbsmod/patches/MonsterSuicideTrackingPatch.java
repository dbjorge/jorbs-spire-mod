package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import javassist.CtBehavior;
import stsjorbsmod.util.ReflectionUtils;

public class MonsterSuicideTrackingPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = SpirePatch.CLASS
    )
    public static class IsMonsterSuicidingField {
        @SuppressWarnings("unchecked")
        public static SpireField<Boolean> isSuiciding = new SpireField(() -> false);
    }

    @SpirePatch(
            clz = SuicideAction.class,
            method = "update"
    )
    public static class SuicideAction_trackIsMonsterSuiciding {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(SuicideAction __this) {
            AbstractMonster m = ReflectionUtils.getPrivateField(__this, SuicideAction.class, "m");
            IsMonsterSuicidingField.isSuiciding.set(m, true);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "die");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    // SnakeDagger is a special case where it suicides without using a SuicideAction
    @SpirePatch(
            clz = SnakeDagger.class,
            method = "takeTurn"
    )
    public static class SnakeDagger_trackIsMonsterSuiciding {
        @SpirePostfixPatch
        public static void patch(SnakeDagger __this) {
            if (__this.nextMove == 2) {
                IsMonsterSuicidingField.isSuiciding.set(__this, true);
            }
        }
    }
}
