package stsjorbsmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.BottledMemoryPatch;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;
import static stsjorbsmod.patches.EntombedPatch.isEntombed;

public class CardUtils {
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
}
