package stsjorbsmod.relics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.PatienceMemory;

import java.util.ArrayList;
import java.util.HashMap;

import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

// Start each fight remembering Patience. At the end of each fight, gain 1hp per Clarity.
public class GrimoireRelic extends CustomJorbsModRelic implements RelicStatsModSupportIntf {
    public static final String ID = JorbsMod.makeID(GrimoireRelic.class);

    private static final int HEAL_PER_CLARITY = 1;
    private static final String STAT_AMOUNT_HEALED = "Amount Healed: ";

    private HashMap<String, Integer> stats = new HashMap<>();

    public GrimoireRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
        resetStats();
    }

    @Override
    public void atBattleStart() {
        AbstractPlayer p = AbstractDungeon.player;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RememberSpecificMemoryAction(p, PatienceMemory.STATIC.ID));
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();

        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;

        if (p.currentHealth > 0) {
            int healAmount = MemoryManager.forPlayer(p).countCurrentClarities() * HEAL_PER_CLARITY;
            p.heal(healAmount);
            stats.merge(STAT_AMOUNT_HEALED, healAmount, Math::addExact);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        GrimoireRelic copy = new GrimoireRelic();
        copy.stats = this.stats;
        return copy;
    }

    @Override
    public String getStatsDescription() {
        return STAT_AMOUNT_HEALED + stats.get(STAT_AMOUNT_HEALED);
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        float amountHealed = (float) stats.get(STAT_AMOUNT_HEALED);
        return new StringBuilder(getStatsDescription())
                .append(STAT_PER_TURN)
                .append(amountHealed / totalTurns)
                .append(STAT_PER_COMBAT)
                .append(amountHealed / totalCombats)
                .toString();
    }

    @Override
    public void resetStats() {
        stats.put(STAT_AMOUNT_HEALED, 0);
    }

    @Override
    public Object getStatsToSave() {
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STAT_AMOUNT_HEALED));
        return statsToSave;
    }

    @Override
    public void setStatsOnLoad(JsonElement jsonElement) {
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        stats.put(STAT_AMOUNT_HEALED, jsonArray.get(0).getAsInt());
    }
}
