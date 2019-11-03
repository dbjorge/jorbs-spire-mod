package stsjorbsmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.HashSet;
import java.util.Set;

public class UniqueCardUtils {
    public static String getUpgradeInclusiveCardID(AbstractCard card) {
        // TODO; should account for Wrath upgrades too
        return card.cardID + "__" + card.timesUpgraded;
    }

    public static int countUniqueCards(CardGroup group) {
        Set<String> uniqueCardIDs = new HashSet<String>();
        group.group.forEach(c -> uniqueCardIDs.add(getUpgradeInclusiveCardID(c)));
        return uniqueCardIDs.size();
    }
}
