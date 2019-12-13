package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;

public class FilterRandomCardGenerationPatch {
    private static void RemovePersistentPositiveEffects(ArrayList<AbstractCard> list) {
        list.removeIf(card -> card.hasTag(PERSISTENT_POSITIVE_EFFECT));
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomCardInCombat",
            paramtypez = { }
    )
    public static class AbstractDungeon_returnTrulyRandomCardInCombat_1 {
        @SpireInsertPatch(
                locator = ArrayList_get_Locator.class,
                localvars = "list"
        )
        public static void patch(ArrayList<AbstractCard> list) {
            RemovePersistentPositiveEffects(list);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomCardInCombat",
            paramtypez = { AbstractCard.CardType.class }
    )
    public static class AbstractDungeon_returnTrulyRandomCardInCombat_2 {
        @SpireInsertPatch(
                locator = ArrayList_get_Locator.class,
                localvars = "list"
        )
        public static void patch(AbstractCard.CardType type, ArrayList<AbstractCard> list) {
            RemovePersistentPositiveEffects(list);
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomColorlessCardInCombat",
            paramtypez = { Random.class }
    )
    public static class AbstractDungeon_returnTrulyRandomColorlessCardInCombat {
        @SpireInsertPatch(
                locator = ArrayList_get_Locator.class,
                localvars = "list"
        )
        public static void patch(Random rng, ArrayList<AbstractCard> list) {
            RemovePersistentPositiveEffects(list);
        }
    }

    // Because most of the random card generation functions are structured so similarly, we use a shared locator.
    // Before a card is picked from the list, filter its contents.
    private static class ArrayList_get_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            final Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}