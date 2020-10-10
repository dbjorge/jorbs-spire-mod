package stsjorbsmod.relics;

import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;

import static stsjorbsmod.JorbsMod.makeID;

/**
 * This interface is meant to help enforce support for the Relic Stats Mod by ForgottenArbiter/Quincunx
 * Steam workshop page: https://steamcommunity.com/sharedfiles/filedetails/?id=2118491069
 * GitHub page: https://github.com/ForgottenArbiter/StsRelicStats
 * For whatever reason, these methods must be defined in the relic class, not any parent class.
 */
interface RelicStatsModSupportIntf {
    String[] STAT_TEXTS = CardCrawlGame.languagePack.getUIString(makeID("RelicStatsModSupportIntf")).TEXT;
    String STAT_PER_TURN = STAT_TEXTS[0];
    String STAT_PER_COMBAT = STAT_TEXTS[1];
    // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
    DecimalFormat STATS_FORMAT = new DecimalFormat("#.###");

    /**
     * The string which is displayed in the stats window (uses Default StS formatting, like NL for a line break)
     */
    String getStatsDescription();

    /**
     * The string which is displayed if extended stats are turned on (per combat, per turn)
     */
    String getExtendedStatsDescription(int totalCombats, int totalTurns);

    /**
     * Called to reset your relic's stats for a new run
     */
    void resetStats();

    JsonElement onSaveStats();

    void onLoadStats(JsonElement jsonElement);

    /**
     * Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
     * Therefore, need to make sure all copies share the same stats by copying the stats.
     * This has been written to be handled in CustomJorbsModRelic
     */
    AbstractRelic makeCopy();
}
