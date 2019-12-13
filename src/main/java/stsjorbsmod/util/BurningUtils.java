package stsjorbsmod.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import stsjorbsmod.relics.AlchemistsFireRelic;

public class BurningUtils {
    public static int calculateNextBurningAmount(AbstractCreature source, int baseAmount) {
        if(source != null && source.isPlayer && ((AbstractPlayer) source).hasRelic(AlchemistsFireRelic.ID)) {
            return AlchemistsFireRelic.CALCULATE_BURNING_AMOUNT.apply(baseAmount);
        } else {
            return 2 * baseAmount / 3;
        }
    }
}
