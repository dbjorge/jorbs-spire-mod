package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;

import java.util.ArrayList;

import static stsjorbsmod.characters.Wanderer.Enums.PERSISTENT_POSITIVE_EFFECT;

public class FilterRandomCardGenerationPatch {
    private static void RemovePersistentPositiveEffects(ArrayList list) {
        list.removeIf(o -> ((AbstractCard) o).hasTag(PERSISTENT_POSITIVE_EFFECT));
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomCardInCombat",
            paramtypez = { }
    )
    public static class AbstractDungeon_returnTrulyRandomCardInCombat_1 {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = "list"
        )
        @SuppressWarnings("unchecked")
        public static void patch(@ByRef ArrayList[] list) {
            RemovePersistentPositiveEffects(list[0]);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomCardInCombat",
            paramtypez = { AbstractCard.CardType.class }
    )
    public static class AbstractDungeon_returnTrulyRandomCardInCombat_2 {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = "list"
        )
        @SuppressWarnings("unchecked")
        public static void patch(AbstractCard.CardType type, @ByRef ArrayList[] list) {
            RemovePersistentPositiveEffects(list[0]);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomColorlessCardInCombat",
            paramtypez = { Random.class }
    )
    public static class AbstractDungeon_returnTrulyRandomColorlessCardInCombat {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = "list"
        )
        @SuppressWarnings("unchecked")
        public static void patch(Random rng, @ByRef ArrayList[] list) {
            RemovePersistentPositiveEffects(list[0]);
        }
    }

    // Because all of the random card generation functions are structured so similarly, we use the same locator.
    // Before a card is picked from the list, filter its contents.
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            final Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}