package stsjorbsmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import stsjorbsmod.patches.WrathField;

import java.util.HashSet;
import java.util.Set;

public class UniqueCardUtils {
    public static String getUpgradeInclusiveCardID(AbstractCard card) {
        return card.cardID + "__" + card.misc + "__" + card.timesUpgraded + "__" + WrathField.wrathEffectCount.get(card);
    }

    public static int countUniqueCards(CardGroup group) {
        Set<String> uniqueCardIDs = new HashSet<>();
        group.group.forEach(c -> uniqueCardIDs.add(getUpgradeInclusiveCardID(c)));
        return uniqueCardIDs.size();
    }
}
