package stsjorbsmod.relics;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * This interface is meant to help enforce support for the Relic Stats Mod by ForgottenArbiter/Quincunx
 * Steam workshop page: https://steamcommunity.com/sharedfiles/filedetails/?id=2118491069
 * GitHub page: https://github.com/ForgottenArbiter/StsRelicStats
 */
interface RelicStatsModSupportIntf {
    String STAT_PER_TURN = " NL Per turn: ";
    String STAT_PER_COMBAT = " NL Per combat: ";

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

    Object getStatsToSave();

    default JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        return gson.toJsonTree(getStatsToSave());
    }

    void setStatsOnLoad(JsonElement jsonElement);

    default void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            setStatsOnLoad(jsonElement);
        } else {
            resetStats();
        }
    }

    /**
     * Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
     * Therefore, need to make sure all copies share the same stats by copying the stats.
     */
    AbstractRelic makeCopy();
}
