package stsjorbsmod.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.*;

import static stsjorbsmod.JorbsMod.makeID;

public abstract class CustomJorbsModIntStatsRelic extends CustomJorbsModRelic {

    protected ArrayList<Integer> stats;
    private static String[] PER_STRINGS = CardCrawlGame.languagePack.getUIString(makeID("RelicStats")).TEXT;

    public CustomJorbsModIntStatsRelic(final String id, final AbstractCard.CardColor relicColor, final RelicTier relicTier, final LandingSound landingSound) {
        super(id, relicColor, relicTier, landingSound);
        resetStats();
    }

    public String getStatsDescription() {
        String statName = DESCRIPTIONS[DESCRIPTIONS.length - 1];
        return statName + stats.get(0);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());
        float stat = (float)stats.get(0);
        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
        // Per turn stats
        builder.append(PER_STRINGS[0]);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        // Per combat stats
        builder.append(PER_STRINGS[1]);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));
        return builder.toString();
    }

    public void resetStats() {
        stats = new ArrayList<>();
        stats.add(0);
    }

    public void addStats(int amount) {
        stats.set(0, stats.get(0) + amount);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        return gson.toJsonTree(stats);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            resetStats();
            stats.set(0, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        CustomJorbsModIntStatsRelic r = (CustomJorbsModIntStatsRelic)super.makeCopy();
        r.stats = this.stats;
        return r;
    }

}
