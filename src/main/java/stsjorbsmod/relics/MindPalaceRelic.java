package stsjorbsmod.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

public class MindPalaceRelic extends CustomJorbsModRelic implements OnModifyMemoriesSubscriber, CustomSavable<String> {
    public static final String ID = JorbsMod.makeID(MindPalaceRelic.class);

    private static String trackedMemoryID = null;

    public MindPalaceRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        if (trackedMemoryID != null) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainSpecificClarityAction(AbstractDungeon.player, trackedMemoryID));
            trackedMemoryID = null;
        }
    }

    @Override
    public void onEquip() {
        MemoryManager mm = MemoryManager.forPlayer();
        if (mm != null && mm.currentMemory != null) {
            trackedMemoryID = mm.currentMemory.ID;
            this.beginPulse();
        }
    }

    @Override
    public void onRememberMemory(String id) {
        trackedMemoryID = id;
        this.beginPulse();
    }

    @Override
    public void onForgetMemory() {
        trackedMemoryID = null;
        this.stopPulse();
    }

    @Override
    public void onSnap() {
        trackedMemoryID = null;
        this.stopPulse();
    }

    @Override
    public String onSave() {
        return trackedMemoryID;
    }

    @Override
    public void onLoad(String savedString) {
        trackedMemoryID = savedString;
    }
}
