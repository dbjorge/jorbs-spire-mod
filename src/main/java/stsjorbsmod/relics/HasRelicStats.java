package stsjorbsmod.relics;

import com.google.gson.JsonElement;

public interface HasRelicStats {

    // The string which is displayed in the stats window (uses Default StS formatting, like NL for a line break)
    String getStatsDescription();

    // The string which is displayed if extended stats are turned on (per combat, per turn)
    String getExtendedStatsDescription(int totalCombats, int totalTurns);

    // Called to reset the relic's stats for a new run
    void resetStats();

    // Returns a JSON object to save the relic's stats
    JsonElement onSaveStats();

    // Loads teh stats from the object returned by onSaveStats()
    void onLoadStats(JsonElement jsonElement);

}
