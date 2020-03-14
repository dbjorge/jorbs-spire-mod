package stsjorbsmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.patches.BottledMemoryPatch;
import stsjorbsmod.patches.DeckOfToilsPatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;
import static stsjorbsmod.patches.EntombedPatch.isEntombed;

public class CardUtils {
    public static final Logger logger = LogManager.getLogger(CardUtils.class.getName());

    /**
     * Returns all the bottleable cards in the parameter CardGroup. Since CardGroup::getPurgeableCards is patched to
     * never return Legendary cards, we add them back into this CardGroup because they can be bottled.
     *
     * @param purgeableCards Likely the result of masterdeck.getPurgeableCards
     * @return all bottleable cards from the CardGroup passed in.
     */
    public static CardGroup getCardsForBottling(CardGroup purgeableCards) {
        ArrayList<AbstractCard> originalCardList = new ArrayList<>(purgeableCards.group);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(LEGENDARY)) {
                originalCardList.add(c);
            }
        }
        CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : originalCardList) {
            // removes entombed and bottled cards from the CardGroup
            if (CardUtils.isBottleable(c)) {
                result.group.add(c);
            }
        }
        return result;
    }

    /**
     * A card is considered bottleable if it is not entombed and not already bottled
     *
     * @param c AbstractCard to check
     * @return true if the card is bottleable, false otherwise
     */
    public static boolean isBottleable(AbstractCard c) {
        return !isEntombed(c) && !c.inBottleTornado && !c.inBottleFlame && !c.inBottleLightning && !BottledMemoryPatch.AbstractCardMemoryFields.inBottleMemory.get(c);
    }

    public static void removeCardsWithCardTags(ArrayList<AbstractCard> list, AbstractCard.CardTags... types) {
        list.removeIf(c -> Arrays.stream(types).anyMatch(t -> c.hasTag(t)));
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

    public static void removeCardsWithTagsFromPools(AbstractCard card) {
        if (card.hasTag(LEGENDARY)) {
            removeCardFromPools(card);
        }
    }
}
