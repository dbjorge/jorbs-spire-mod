package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.Astrolabe;
import com.megacrit.cardcrawl.relics.DollysMirror;
import com.megacrit.cardcrawl.relics.EmptyCage;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class LegendaryPatch {
    private static final String LEGENDARY_QUALIFIED_NAME = "stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY";
    private static final String DraftMod = "Draft";
    private static final String SealedMod = "SealedDeck";

    private static void removeLegendaryCards(ArrayList<AbstractCard> list) {
        list.removeIf(c -> c.hasTag(LEGENDARY));
    }

    public static boolean doesStartingDeckNeedFullPools() {
        return ModHelper.isModEnabled(DraftMod) || ModHelper.isModEnabled(SealedMod);
    }

    public static void removeLegendaryCardsFromPools() {
        Predicate<AbstractCard> isLegendary = c -> c.hasTag(JorbsMod.JorbsCardTags.LEGENDARY);

        // AbstractDungeon has many returnRandam*, returnTrulyRandom*, and *transformCard methods that use these pools.
        AbstractDungeon.colorlessCardPool.group.removeIf(isLegendary);
        AbstractDungeon.srcColorlessCardPool.group.removeIf(isLegendary);
        AbstractDungeon.commonCardPool.group.removeIf(isLegendary);
        AbstractDungeon.srcCommonCardPool.group.removeIf(isLegendary);
        AbstractDungeon.uncommonCardPool.group.removeIf(isLegendary);
        AbstractDungeon.srcUncommonCardPool.group.removeIf(isLegendary);
        AbstractDungeon.rareCardPool.group.removeIf(isLegendary);
        AbstractDungeon.srcRareCardPool.group.removeIf(isLegendary);
        AbstractDungeon.curseCardPool.group.removeIf(isLegendary);
        AbstractDungeon.srcCurseCardPool.group.removeIf(isLegendary);

        // AbstractDungeon.transformCard can call getCurse to generate a replacement curse.
        HashMap<String, AbstractCard> curses = ReflectionUtils.getPrivateField(null, CardLibrary.class, "curses");
        ArrayList<String> removals = new ArrayList<>();
        curses.forEach((s, c) -> {
            if (isLegendary.test(c)) {
                removals.add(s);
            };
        });
        removals.forEach(s -> curses.remove(s));
    }

    // Note: this is called only by edited expressions in the main game. See the derived ExprEditor that follows.
    public static CardGroup cloneCardGroupWithoutLegendaryCards(CardGroup original) {
        CardGroup copy = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        copy.group = new ArrayList<>(original.group);
        removeLegendaryCards(copy.group);
        return copy;
    }

    private static class CloneMasterDeckWithoutLegendaryCardsEditor extends ExprEditor {
        @Override
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            // Replace the "AbstractPlayer.masterDeck" with a new list that filters out the legendary cards.
            if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName()) && fieldAccess.getFieldName().equals("masterDeck")) {
                fieldAccess.replace("{ $_ = " + LegendaryPatch.class.getName() + ".cloneCardGroupWithoutLegendaryCards($proceed()); }");
            }
        }
    }

    // Legendary cards aren't purgeable. By removing them from choices to purge, we sidestep them even being picked
    // by the player to remove or transform.
    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class CardGroup_getPurgeableCards {
        @SpirePostfixPatch
        public static CardGroup patch(CardGroup result, CardGroup instance) {
            removeLegendaryCards(result.group);
            return result;
        }
    }

    // While we filter Legendary cards from being in the random reward pools after the dungeon initializes, if the
    // player has the Prismatic Shard relic, this particular method is also used in AbstractDungeon.getRewardCards.
    // We don't want to modify the overall list of cards that the CardLibrary draws from, hence this patch.
    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard",
            paramtypez = { AbstractCard.CardRarity.class })
    public static class CardLibrary_getAnyColorCard {
        @SpireInsertPatch(locator = CardGroup_shuffle_Locator.class, localvars = "anyCard")
        public static void patch(AbstractCard.CardRarity rarity, CardGroup anyCard) {
            removeLegendaryCards(anyCard.group);
        }
    }

    // The Falling event removes cards from the deck. It selects them with these methods in CardHelper, and it is
    // the only consumer of these methods. We effectively replace the whole body be redoing the method's work.
    @SpirePatch(clz = CardHelper.class, method = "hasCardWithType")
    public static class CardHelper_hasCardWithType {
        @SpirePostfixPatch
        public static boolean patch(AbstractCard.CardType type) {
            return CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group.stream()
                    .anyMatch(c -> c.type == type && !c.hasTag(LEGENDARY));
        }
    }

    @SpirePatch(clz = CardHelper.class, method = "returnCardOfType")
    public static class CardHelper_returnCardOfType {
        @SpirePostfixPatch
        public static AbstractCard patch(AbstractCard.CardType type, Random rng) {
            ArrayList<AbstractCard> cards =
                    CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group.stream()
                            .filter(c -> c.type == type && !c.hasTag(LEGENDARY))
                            .collect(Collectors.toCollection(ArrayList::new));
            return cards.remove(rng.random(cards.size() - 1));
        }
    }

    // The Duplicator event makes a copy of any card in the deck. Provide a filtered list to choose from.
    @SpirePatch(clz = Duplicator.class, method = "use")
    public static class Duplicator_use {
        public static ExprEditor Instrument() {
            return new CloneMasterDeckWithoutLegendaryCardsEditor();
        }
    }

    // The Fountain of Curse Removal event should only be eligible if the player has a non-Legendary curse;
    // i.e., one that could be removed.
    @SpirePatch(clz = AbstractPlayer.class, method = "isCursed")
    public static class AbstractPlayer_isCursed {
        public static ExprEditor Instrument() {
            return new CloneMasterDeckWithoutLegendaryCardsEditor();
        }
    }

    // The Fountain of Curse Removal event removes curse cards from the deck. It selects them directly from the
    // masterDeck. We don't let it remove any Legendary curses.
    @SpirePatch(clz = FountainOfCurseRemoval.class, method = "buttonEffect")
    public static class FountainOfCurseRemoval_buttonEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    // replacing the "((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).inBottleFlame"
                    // expression in the original if statement to add in an "|| card.hasTag(LEGENDARY)"
                    if (fieldAccess.getClassName().equals(AbstractCard.class.getName()) && fieldAccess.getFieldName().equals("inBottleFlame")) {
                        fieldAccess.replace("{ $_ = ($proceed() || $0.hasTag(" + LEGENDARY_QUALIFIED_NAME + ")); }");
                    }
                }
            };
        }
    }

    // The We Meet Again event selects a non-basic, non-curse card. We ensure it's also non-legendary.
    @SpirePatch(clz = WeMeetAgain.class, method = "getRandomNonBasicCard")
    public static class WeMeetAgain_getRandomNonBasicCard {
        @SpireInsertPatch(locator = ArrayList_isEmpty_Locator.class, localvars = "list")
        public static void patch(ArrayList<AbstractCard> list) {
            removeLegendaryCards(list);
        }
    }

    // Astrolabe builds its own list of cards to transform. Filter that list.
    @SpirePatch(clz = Astrolabe.class, method = "onEquip")
    public static class Astrolabe_onEquip {
        @SpireInsertPatch(locator = ArrayList_isEmpty_Locator.class, localvars = "tmp")
        public static void patch(CardGroup tmp) {
            removeLegendaryCards(tmp.group);
        }
    }

    // Dolly's Mirror duplicates a card in the masterDeck. Filter that first.
    @SpirePatch(clz = DollysMirror.class, method = "onEquip")
    public static class DollysMirror_onEquip {
        public static ExprEditor Instrument() {
            return new CloneMasterDeckWithoutLegendaryCardsEditor();
        }
    }

    // Empty Cage optimizes the user's choice, removing all eligible cards if <= 2. Filter that list.
    @SpirePatch(clz = EmptyCage.class, method = "onEquip")
    public static class EmptyCage_onEquip {
        @SpireInsertPatch(locator = ArrayList_isEmpty_Locator.class, localvars = "tmp")
        public static void patch(CardGroup tmp) {
            removeLegendaryCards(tmp.group);
        }
    }

    // We expect to have patched all situations in the main game. These four routines are almost universally invoked
    // to do the disallowed work of removing or transforming a Legendary card. Log any such instances encountered.
    @SpirePatch(clz = CardGroup.class, method = "removeCard",
            paramtypez = { AbstractCard.class })
    public static class CardGroup_removeCard_1 {
        @SpirePrefixPatch
        public static void patch(CardGroup instance, AbstractCard c) {
            if (instance.type == CardGroup.CardGroupType.MASTER_DECK && c.hasTag(LEGENDARY)) {
                JorbsMod.logger.error("Legendary card removed from Master Deck.");
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard",
            paramtypez = { String.class })
    public static class CardGroup_removeCard_2 {
        @SpirePrefixPatch
        public static void patch(CardGroup instance, String targetID) {
            if (instance.type == CardGroup.CardGroupType.MASTER_DECK) {
                if (instance.group.stream().anyMatch(c -> c.cardID.equals(targetID) && c.hasTag(LEGENDARY))) {
                    JorbsMod.logger.error("Legendary card removed by cardID from Master Deck.");
                }
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "transformCard",
            paramtypez = { AbstractCard.class, boolean.class, Random.class })
    public static class AbstractDungeon_transformCard {
        @SpirePostfixPatch
        public static void patch(AbstractCard c, boolean autoUpgrade, Random rng) {
            // Because the transformed card is replaced, this can incorrectly mark cards as seen in the UnlockTracker.
            if (c.hasTag(LEGENDARY)) {
                JorbsMod.logger.error(
                        "AbstractDungeon.transformCard invoked on Legendary card. "
                                + "Setting original card as the transform.");
                AbstractDungeon.transformedCard = c;
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "srcTransformCard")
    public static class AbstractDungeon_srcTransformCard {
        @SpirePostfixPatch
        public static void patch(AbstractCard c) {
            if (c.hasTag(LEGENDARY)) {
                JorbsMod.logger.error(
                        "AbstractDungeon.srcTransformCard invoked on Legendary card. "
                                + "Setting original card as the transform.");
                AbstractDungeon.transformedCard = c;
            }
        }
    }

    // Mod support for Draft. After the player picks cards, remove Legendary cards from the reward pools.
    @SpirePatch(clz = CardRewardScreen.class, method = "onClose")
    public static class CardRewardScreen_onClose {
        @SpirePostfixPatch
        public static void patch(CardRewardScreen instance) {
            boolean isDrafting = ReflectionUtils.getPrivateField(instance, CardRewardScreen.class, "draft");
            if (isDrafting) {
                removeLegendaryCardsFromPools();
            }
        }
    }

    // Mod support for SealedDeck. After the starting deck is created, remove Legendary cards from the reward pools.
    @SpirePatch(clz = NeowEvent.class, method = "dailyBlessing")
    public static class NeowEvent_dailyBlessing {
        @SpirePostfixPatch
        public static void patch(NeowEvent instance) {
            if (ModHelper.isModEnabled(SealedMod)) {
                removeLegendaryCardsFromPools();
            }
        }
    }

    // ---------- LOCATORS ----------

    private static class ArrayList_add_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            final Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    private static class ArrayList_isEmpty_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            final Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }

    private static class CardGroup_shuffle_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            final Matcher matcher = new Matcher.MethodCallMatcher(CardGroup.class, "shuffle");
            return LineFinder.findInOrder(ctMethodToPatch, matcher);
        }
    }
}
