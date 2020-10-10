package stsjorbsmod.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;
import java.util.HashMap;

import static stsjorbsmod.JorbsMod.makeID;
import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

/**
 * Reduces Burning's falloff rate to 10%
 */
public class AlchemistsFireRelic extends CustomJorbsModRelic implements ClickableRelic, RelicStatsModSupportIntf {
    public static final String ID = JorbsMod.makeID(AlchemistsFireRelic.class);

    public static final String[] ON_CLICK = CardCrawlGame.languagePack.getUIString(makeID("AlchemistsFireOnClick")).TEXT;

    public static final int BURNING_FALLOFF_RATE = 10;

    private final String STAT_BURNING_KEPT = DESCRIPTIONS[1];
    private HashMap<String, Integer> stats = new HashMap<>();

    public AlchemistsFireRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.MAGICAL);
        resetStats();
    }

    @Override
    public void atBattleStart() {
        this.flash();
    }

    public int calculateBurningAmount(int amount) {
        return (amount * (100 - BURNING_FALLOFF_RATE)) / 100;
    }

    public void updateStats(int amount) {
        stats.merge(STAT_BURNING_KEPT, amount, Math::addExact);
    }

    @Override
    public void onRightClick() {
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, ON_CLICK[0], true));
    }

    @Override
    public AbstractRelic makeCopy() {
        AlchemistsFireRelic copy = (AlchemistsFireRelic) super.makeCopy();
        copy.stats = stats;
        return copy;
    }

    @Override
    public String getStatsDescription() {
        return String.format(STAT_BURNING_KEPT, stats.get(STAT_BURNING_KEPT));
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        float burningKept = (float) stats.get(STAT_BURNING_KEPT);
        return new StringBuilder()
                .append(getStatsDescription())
                .append(String.format(STAT_PER_TURN, STATS_FORMAT.format(burningKept / Math.max(totalTurns, 1))))
                .append(String.format(STAT_PER_COMBAT, STATS_FORMAT.format(burningKept / Math.max(totalCombats, 1))))
                .toString();
    }

    @Override
    public void resetStats() {
        stats.put(STAT_BURNING_KEPT, 0);
    }

    @Override
    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STAT_BURNING_KEPT));
        return gson.toJsonTree(statsToSave);
    }

    @Override
    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STAT_BURNING_KEPT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
