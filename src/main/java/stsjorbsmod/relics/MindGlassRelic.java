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
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.powers.MindGlassPower;

import java.util.ArrayList;
import java.util.HashMap;

import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

/**
 * When gaining a unique clarity, deals 3 damage to all enemies.
 * When gaining the tenth clarity in a combat, deal 100 damage to all enemies.
 */
public class MindGlassRelic extends CustomJorbsModRelic implements OnModifyMemoriesSubscriber, PostUpdateSubscriber, RelicStatsModSupportIntf {
    public static final String ID = JorbsMod.makeID(MindGlassRelic.class);

    private static final int ONE_CLARITY_DAMAGE = 3;
    private static final int TEN_CLARITY_DAMAGE = 100;
    private static final String STAT_DAMAGE_DEALT = "Damage Dealt: ";
    private static final String STAT_TEN_CLARITIES = "Times Ten Clarities Gained: ";

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
                            new MindGlassPower(AbstractDungeon.player, TEN_CLARITY_DAMAGE),
                            1,
                            true));
        }
        int[] damageMatrix = DamageInfo.createDamageMatrix(ONE_CLARITY_DAMAGE, true);
        updateStats(damageMatrix, false);
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        null,
                        damageMatrix,
                        DamageInfo.DamageType.NORMAL,
                        // TODO: More impactful and relevant FX. See FlashAtkImgEffect.loadImage() and
                        //  FlashAtkImgEffect.playSound() for usage of AttackEffect in base game.
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
        BaseMod.unsubscribe(this);
    }

    public void updateStats(int[] damageMatrix, boolean isTenClarities) {
        for (int damage : damageMatrix) {
            stats.merge(STAT_DAMAGE_DEALT, damage, Math::addExact);
        }
        if (isTenClarities) {
            stats.merge(STAT_TEN_CLARITIES, 1, Math::addExact);
        }
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

    @Override
    public String getStatsDescription() {
        return new StringBuilder(STAT_DAMAGE_DEALT)
                .append(stats.get(STAT_DAMAGE_DEALT))
                .append(" NL ")
                .append(STAT_TEN_CLARITIES)
                .append(stats.get(STAT_TEN_CLARITIES))
                .toString();
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        float damageDealt = stats.get(STAT_DAMAGE_DEALT);
        float tenClarities = stats.get(STAT_TEN_CLARITIES);
        return new StringBuilder(STAT_DAMAGE_DEALT)
                .append((int) damageDealt)
                .append(STAT_PER_TURN)
                .append(damageDealt / totalTurns)
                .append(STAT_PER_COMBAT)
                .append(damageDealt / totalCombats)
                .append(" NL ")
                .append(STAT_TEN_CLARITIES)
                .append((int) tenClarities)
                .append(STAT_PER_TURN)
                .append(tenClarities / totalTurns)
                .append(STAT_PER_COMBAT)
                .append(tenClarities / totalCombats)
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
