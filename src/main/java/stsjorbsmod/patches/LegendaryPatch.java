package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.*;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

// Legendary cards (cards with the LEGENDARY tag) have the following special qualities:
// * Cannot be duplicated, removed, or transformed
// * Can only be obtained once per run (are removed from pools after being obtained the first time)
// * (not implemented yet) Explorer Legendaries can only be found after the Act 2 boss
public class LegendaryPatch {
    public static final Logger logger = LogManager.getLogger(LegendaryPatch.class.getName());

    private static final String LEGENDARY_QUALIFIED_NAME = "stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY";
    private static final String DraftMod = "Draft";
    private static final String SealedMod = "SealedDeck";

    private static void removeLegendaryCards(ArrayList<AbstractCard> list) {
        list.removeIf(c -> c.hasTag(LEGENDARY));
    }

    public static boolean doesStartingDeckNeedFullPools() {
        return ModHelper.isModEnabled(DraftMod) || ModHelper.isModEnabled(SealedMod);
    }

    public static void removeCardFromPools(AbstractCard card) {
        logger.info(String.format("Removing card %1$s (%2$s/%3$s/%4$s) from applicable pools", card.cardID, card.rarity, card.color, card.type));
        Predicate<AbstractCard> isMatch = c -> c.cardID.equals(card.cardID);

        // AbstractDungeon has many returnRandom*, returnTrulyRandom*, and *transformCard methods that use these pools.
        if (card.rarity == AbstractCard.CardRarity.COMMON) {
            AbstractDungeon.commonCardPool.group.removeIf(isMatch);
            AbstractDungeon.srcCommonCardPool.group.removeIf(isMatch);
        } else if (card.rarity == AbstractCard.CardRarity.UNCOMMON) {
            AbstractDungeon.uncommonCardPool.group.removeIf(isMatch);
            AbstractDungeon.srcUncommonCardPool.group.removeIf(isMatch);
        } else if (card.rarity == AbstractCard.CardRarity.RARE) {
            AbstractDungeon.rareCardPool.group.removeIf(isMatch);
            AbstractDungeon.srcRareCardPool.group.removeIf(isMatch);
        }

        // Note: color pools can overlap with rarity pools
        if (card.color == AbstractCard.CardColor.COLORLESS) {
            AbstractDungeon.colorlessCardPool.group.removeIf(isMatch);
            AbstractDungeon.srcColorlessCardPool.group.removeIf(isMatch);
        }
        if (card.color == AbstractCard.CardColor.CURSE || card.rarity == AbstractCard.CardRarity.CURSE) {
            AbstractDungeon.curseCardPool.group.removeIf(isMatch);
            AbstractDungeon.srcCurseCardPool.group.removeIf(isMatch);

            // AbstractDungeon.transformCard can call getCurse directly to generate a replacement curse.
            HashMap<String, AbstractCard> curses = ReflectionUtils.getPrivateField(null, CardLibrary.class, "curses");
            curses.remove(card.cardID);
        }
    }

    public static void removeObtainedLegendaryCardsFromPools() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(LEGENDARY)) {
                removeCardFromPools(c);
            }
        }
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

    // This handles removing legendary cards from the pool after they are obtained the first time
    @SpirePatch(clz = Soul.class, method = "obtain")
    public static class Soul_obtain {
        @SpirePostfixPatch
        public static void patch(Soul __this, AbstractCard card) {
            if (card.hasTag(LEGENDARY)) {
                removeCardFromPools(card);
            }
        }
    }

    // Card pools get reinitialized when loading saves and when moving between floors; this handles both
    // Note that this *cannot* use BaseMod.receivePostDungeonInitialize (it's patched in prior to initializeCardPools)
    @SpirePatch(clz = AbstractDungeon.class, method = "initializeCardPools")
    public static class AbstractDungeon_initializeCardPools {
        @SpirePostfixPatch
        public static void patch(AbstractDungeon __this) {
            LegendaryPatch.removeObtainedLegendaryCardsFromPools();
        }
    }

    // Allow for CardGroup::getPurgeableCards to contain Legendary cards occasionally. Since the bottle relics use
    // CardGroup::getPurgeableCards, we want to keep them in the group if being called via one of the bottle relics.
    @SpirePatch(clz = CardGroup.class, method = SpirePatch.CLASS)
    public static class CardGroupFields {
        public static SpireField<Boolean> isContainsLegendaryCards = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = BottledFlame.class, method = "onEquip")
    public static class BottledFlame_onEquip {
        @SpirePrefixPatch
        public static void Prefix(BottledFlame __instance) {
            CardGroupFields.isContainsLegendaryCards.set(AbstractDungeon.player.masterDeck, true);
        }

        @SpirePostfixPatch
        public static void Postfix(BottledFlame __instance) {
            CardGroupFields.isContainsLegendaryCards.set(AbstractDungeon.player.masterDeck, false);
        }
    }

    @SpirePatch(clz = BottledLightning.class, method = "onEquip")
    public static class BottledLightning_onEquip {
        @SpirePrefixPatch
        public static void Prefix(BottledLightning __instance) {
            CardGroupFields.isContainsLegendaryCards.set(AbstractDungeon.player.masterDeck, true);
        }

        @SpirePostfixPatch
        public static void Postfix(BottledLightning __instance) {
            CardGroupFields.isContainsLegendaryCards.set(AbstractDungeon.player.masterDeck, false);
        }
    }

    @SpirePatch(clz = BottledTornado.class, method = "onEquip")
    public static class BottledTornado_onEquip {
        @SpirePrefixPatch
        public static void Prefix(BottledTornado __instance) {
            CardGroupFields.isContainsLegendaryCards.set(AbstractDungeon.player.masterDeck, true);
        }

        @SpirePostfixPatch
        public static void Postfix(BottledTornado __instance) {
            CardGroupFields.isContainsLegendaryCards.set(AbstractDungeon.player.masterDeck, false);
        }
    }

    // Legendary cards aren't purgeable. By removing them from choices to purge, we sidestep them even being picked
    // by the player to remove or transform.
    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class CardGroup_getPurgeableCards {
        @SpirePostfixPatch
        public static CardGroup patch(CardGroup result, CardGroup instance) {
            if (!CardGroupFields.isContainsLegendaryCards.get(instance)) {
                removeLegendaryCards(result.group);
            }
            return result;
        }
    }

    // While we filter Legendary cards from being in the random reward pools after the dungeon initializes, if the
    // player has the Prismatic Shard relic, this particular method is also used in AbstractDungeon.getRewardCards.
    // We don't want to modify the overall list of cards that the CardLibrary draws from, hence this patch.
    @SpirePatch(clz = CardLibrary.class, method = "getAnyColorCard",
            paramtypez = {AbstractCard.CardRarity.class})
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
            paramtypez = {AbstractCard.class})
    public static class CardGroup_removeCard_1 {
        @SpirePrefixPatch
        public static void patch(CardGroup instance, AbstractCard c) {
            if (instance.type == CardGroup.CardGroupType.MASTER_DECK && c.hasTag(LEGENDARY)) {
                logger.error("Legendary card removed from Master Deck.");
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard",
            paramtypez = {String.class})
    public static class CardGroup_removeCard_2 {
        @SpirePrefixPatch
        public static void patch(CardGroup instance, String targetID) {
            if (instance.type == CardGroup.CardGroupType.MASTER_DECK) {
                if (instance.group.stream().anyMatch(c -> c.cardID.equals(targetID) && c.hasTag(LEGENDARY))) {
                    logger.error("Legendary card removed by cardID from Master Deck.");
                }
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "transformCard",
            paramtypez = {AbstractCard.class, boolean.class, Random.class})
    public static class AbstractDungeon_transformCard {
        @SpirePostfixPatch
        public static void patch(AbstractCard c, boolean autoUpgrade, Random rng) {
            // Because the transformed card is replaced, this can incorrectly mark cards as seen in the UnlockTracker.
            if (c.hasTag(LEGENDARY)) {
                logger.error(
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
                logger.error(
                        "AbstractDungeon.srcTransformCard invoked on Legendary card. "
                                + "Setting original card as the transform.");
                AbstractDungeon.transformedCard = c;
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
