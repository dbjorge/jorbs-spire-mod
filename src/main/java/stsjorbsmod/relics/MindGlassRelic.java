package stsjorbsmod.relics;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.relicStats.AoeDamageFollowupStatsAction;
import stsjorbsmod.actions.relicStats.PreAoeDamageStatsAction;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.powers.MindGlassPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

/**
 * When gaining a unique clarity, deals 3 damage to all enemies.
 * When gaining the tenth clarity in a combat, deal 100 damage to all enemies.
 */
public class MindGlassRelic extends CustomJorbsModRelic implements OnModifyMemoriesSubscriber, PostUpdateSubscriber, RelicStatsModSupportIntf {
    public static final String ID = JorbsMod.makeID(MindGlassRelic.class);

    private static final int ONE_CLARITY_DAMAGE = 3;
    private static final int TEN_CLARITY_DAMAGE = 100;

    private final String STAT_DAMAGE_DEALT = DESCRIPTIONS[1];
    private final String STAT_TEN_CLARITIES = DESCRIPTIONS[2];
    private final Consumer<int[]> statTracker = i -> updateStats(i, false);
    private HashMap<String, Integer> stats = new HashMap<>();

    public MindGlassRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
        resetStats();
    }

    @Override
    public void atPreBattle() {
        BaseMod.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onGainClarity(String id) {
        ++this.counter;
        this.flash();
        if (this.counter == 10) {
            this.stopPulse();
        } else if (this.counter == 9) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new MindGlassPower(AbstractDungeon.player, TEN_CLARITY_DAMAGE, statTracker),
                            1,
                            true));
        }
        PreAoeDamageStatsAction preAoeDamageStatsAction = new PreAoeDamageStatsAction();
        AbstractDungeon.actionManager.addToBottom(preAoeDamageStatsAction);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        null,
                        DamageInfo.createDamageMatrix(ONE_CLARITY_DAMAGE, true),
                        DamageInfo.DamageType.NORMAL,
                        // TODO: More impactful and relevant FX. See FlashAtkImgEffect.loadImage() and
                        //  FlashAtkImgEffect.playSound() for usage of AttackEffect in base game.
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(
                new AoeDamageFollowupStatsAction(statTracker, preAoeDamageStatsAction)
        );
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
        BaseMod.unsubscribe(this);
    }

    /**
     * After every game update, if this instance of the relic was subscribed to the post relic, check if it should pulse
     */
    @Override
    public void receivePostUpdate() {
        if (this.counter == 9 && !pulse && flashTimer == 0) {
            this.beginLongPulse();
        }
    }

    public void updateStats(int[] damageMatrix, boolean isTenClarities) {
        for (int damage : damageMatrix) {
            stats.merge(STAT_DAMAGE_DEALT, damage, Math::addExact);
        }
        if (isTenClarities) {
            stats.merge(STAT_TEN_CLARITIES, 1, Math::addExact);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        MindGlassRelic copy = (MindGlassRelic) super.makeCopy();
        copy.stats = stats;
        return copy;
    }

    @Override
    public String getStatsDescription() {
        return new StringBuilder()
                .append(String.format(STAT_DAMAGE_DEALT, stats.get(STAT_DAMAGE_DEALT)))
                .append(String.format(STAT_TEN_CLARITIES, stats.get(STAT_TEN_CLARITIES)))
                .toString();
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        float damageDealt = stats.get(STAT_DAMAGE_DEALT);
        return new StringBuilder()
                .append(String.format(STAT_DAMAGE_DEALT, stats.get(STAT_DAMAGE_DEALT)))
                .append(String.format(STAT_PER_TURN, STATS_FORMAT.format(damageDealt / Math.max(totalTurns, 1))))
                .append(String.format(STAT_PER_COMBAT, STATS_FORMAT.format(damageDealt / Math.max(totalCombats, 1))))
                .append(String.format(STAT_TEN_CLARITIES, stats.get(STAT_TEN_CLARITIES)))
                .toString();
    }

    @Override
    public void resetStats() {
        stats.put(STAT_DAMAGE_DEALT, 0);
    }

    @Override
    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STAT_DAMAGE_DEALT));
        statsToSave.add(stats.get(STAT_TEN_CLARITIES));
        return gson.toJsonTree(statsToSave);
    }

    @Override
    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STAT_DAMAGE_DEALT, jsonArray.get(0).getAsInt());
            stats.put(STAT_TEN_CLARITIES, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }
}
