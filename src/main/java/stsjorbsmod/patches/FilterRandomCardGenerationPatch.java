package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;
import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;

public class FilterRandomCardGenerationPatch {
    private static void RemovePersistentPositiveEffects(ArrayList<AbstractCard> list) {
        list.removeIf(card -> card.hasTag(PERSISTENT_POSITIVE_EFFECT));
    }

    private static void RemoveLegendaryCards(ArrayList<AbstractCard> list) {
        list.removeIf(card -> card.hasTag(LEGENDARY));
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
        @SuppressWarnings("unchecked")
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
        @SuppressWarnings("unchecked")
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
        @SuppressWarnings("unchecked")
        public static void patch(Random rng, ArrayList<AbstractCard> list) {
            RemovePersistentPositiveEffects(list);
        }
    }

    // While we filter Legendary cards from being in the random reward pools after the dungeon initializes, if the
    // player has the Prismatic Shard relic, this particular method is also used in AbstractDungeon.getRewardCards.
    @SpirePatch(
            clz = CardLibrary.class,
            method = "getAnyColorCard",
            paramtypez = { AbstractCard.CardRarity.class }
    )
    public static class CardLibrary_getAnyColorCard {
        @SpireInsertPatch(
                locator = CardGroup_shuffle_Locator.class,
                localvars = "anyCard"
        )
        @SuppressWarnings("unchecked")
        public static void patch(AbstractCard.CardRarity rarity, CardGroup anyCard) {
            RemoveLegendaryCards(anyCard.group);
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

    // Before the card group is shuffled, filter its contents.
    private static class CardGroup_shuffle_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            final Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "shuffle");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}