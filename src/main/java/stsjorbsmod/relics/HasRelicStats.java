package stsjorbsmod.relics;

import com.google.gson.JsonElement;

// To add Relic Stats integration to a relic, implement this interface
// See https://github.com/ForgottenArbiter/StsRelicStats/wiki/Add-stats-to-your-relics

public interface HasRelicStats {

    // The string which is displayed in the stats window (uses Default StS formatting, like NL for a line break)
    String getStatsDescription();

    // The string which is displayed if extended stats are turned on (per combat, per turn)
    String getExtendedStatsDescription(int totalCombats, int totalTurns);

    // Called to reset the relic's stats for a new run
    void resetStats();

    // Returns a JSON object to save the relic's stats
    JsonElement onSaveStats();

    // Loads the stats from the object returned by onSaveStats()
    void onLoadStats(JsonElement jsonElement);

}
