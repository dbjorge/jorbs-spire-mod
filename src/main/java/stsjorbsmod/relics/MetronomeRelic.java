package stsjorbsmod.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.IncreaseManifestAction;

import java.util.ArrayList;
import java.util.HashMap;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class MetronomeRelic extends CustomJorbsModRelic implements RelicStatsModSupportIntf {
    public static final String ID = JorbsMod.makeID(MetronomeRelic.class);

    private static final String STAT_MANIFEST_REDUCED = "Manifest Reduced: ";

    private HashMap<String, Integer> stats = new HashMap<>();

    public MetronomeRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
        resetStats();
    }

    @Override
    public void onEquip(){this.counter = 0;}

    @Override
    public void atTurnStart(){
        this.counter++;
        if (this.counter == 5){
            this.counter = 0;
            flash();
            addToBot(new IncreaseManifestAction(-1));
            stats.merge(STAT_MANIFEST_REDUCED, 1, Math::addExact);
        }
    }

    @Override
    public String getStatsDescription() {
        return STAT_MANIFEST_REDUCED + stats.get(STAT_MANIFEST_REDUCED);
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        float manifestReduced = (float) stats.get(STAT_MANIFEST_REDUCED);
        return new StringBuilder(getStatsDescription())
                .append(STAT_PER_TURN)
                .append(manifestReduced / totalTurns)
                .append(STAT_PER_COMBAT)
                .append(manifestReduced / totalCombats)
                .toString();
    }

    @Override
    public void resetStats() {
        stats.put(STAT_MANIFEST_REDUCED, 0);
    }

    @Override
    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STAT_MANIFEST_REDUCED));
        return gson.toJsonTree(statsToSave);
    }

    @Override
    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STAT_MANIFEST_REDUCED, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
