package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import com.megacrit.cardcrawl.events.shrines.WeMeetAgain;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.Astrolabe;
import com.megacrit.cardcrawl.relics.DollysMirror;
import com.megacrit.cardcrawl.relics.EmptyCage;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class LegendaryPatch {
    private static final String LEGENDARY_QUALIFIED_NAME = "stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY";
    private static void RemoveLegendaryCards(ArrayList<AbstractCard> list) {
        list.removeIf(c -> c.hasTag(LEGENDARY));
    }
    public static CardGroup CloneCardGroupWithoutLegendaryCards(CardGroup original) {
        CardGroup copy = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        copy.group = (ArrayList<AbstractCard>)original.group.clone();
        RemoveLegendaryCards(copy.group);
        return copy;
    }

    // Legendary cards aren't purgeable. By removing them from choices to purge, we sidestep them even being picked
    // by the player to remove or transform.
    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class CardGroup_getPurgeableCards {
        @SpirePostfixPatch
        public static CardGroup patch(CardGroup result, CardGroup instance) {
            RemoveLegendaryCards(result.group);
            return result;
        }
    }

    // While we filter Legendary cards from being in the random reward pools after the dungeon initializes, if the
    // player has the Prismatic Shard relic, this particular method is also used in AbstractDungeon.getRewardCards.
    // We don't want to modify the overall list of cards that the CardLibrary draws from, hence this patch.
    @SpirePatch(
            clz = CardLibrary.class,
            method = "getAnyColorCard",
            paramtypez = { AbstractCard.CardRarity.class }
    )
    public static class CardLibrary_getAnyColorCard {
        @SpireInsertPatch(locator = CardGroup_shuffle_Locator.class, localvars = "anyCard")
        public static void patch(AbstractCard.CardRarity rarity, CardGroup anyCard) {
            RemoveLegendaryCards(anyCard.group);
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
            RemoveLegendaryCards(list);
        }
    }

    // Astrolabe builds its own list of cards to transform. Filter that list.
    @SpirePatch(clz = Astrolabe.class, method = "onEquip")
    public static class Astrolabe_onEquip {
        @SpireInsertPatch(locator = ArrayList_isEmpty_Locator.class, localvars = "tmp")
        public static void patch(CardGroup tmp) {
            RemoveLegendaryCards(tmp.group);
        }
    }

    // Dolly's Mirror duplicates a card in the masterDeck. Filter that first.
    @SpirePatch(clz = DollysMirror.class, method = "onEquip")
    public static class DollysMirror_onEquip {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    // replacing the "AbstractDungeon.player.masterDeck" parameter passed to gridSelectScreen.open with
                    // a new list that filters out the legendary cards
                    if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName()) && fieldAccess.getFieldName().equals("masterDeck")) {
                        fieldAccess.replace("{ $_ = " + LegendaryPatch.class.getName() + ".CloneCardGroupWithoutLegendaryCards($proceed()); }");
                    }
                }
            };
        }
    }

    // Empty Cage optimizes the user's choice, removing all eligible cards if <= 2. Filter that list.
    @SpirePatch(clz = EmptyCage.class, method = "onEquip")
    public static class EmptyCage_onEquip {
        @SpireInsertPatch(locator = ArrayList_isEmpty_Locator.class, localvars = "tmp")
        public static void patch(CardGroup tmp) {
            RemoveLegendaryCards(tmp.group);
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
