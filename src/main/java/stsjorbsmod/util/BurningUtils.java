package stsjorbsmod.util;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.relics.AlchemistsFireRelic;

public class BurningUtils {

    public static int calculateNextBurningAmount(AbstractCreature source, int baseAmount, boolean simulated) {
        int normalAmount = 2 * baseAmount / 3;
        if(source != null && source.isPlayer && ((AbstractPlayer) source).hasRelic(AlchemistsFireRelic.ID)) {
            int alchemistAmount = AlchemistsFireRelic.CALCULATE_BURNING_AMOUNT.apply(baseAmount);
            AlchemistsFireRelic relic = (AlchemistsFireRelic) AbstractDungeon.player.getRelic(AlchemistsFireRelic.ID);
            if (!simulated) {
                relic.addStats(alchemistAmount - normalAmount);
            }
            return alchemistAmount;
        } else {
            return normalAmount;
        }
    }

    public static int calculateNextBurningAmount(AbstractCreature source, int baseAmount) {
        return calculateNextBurningAmount(source, baseAmount, true);
    }
}
