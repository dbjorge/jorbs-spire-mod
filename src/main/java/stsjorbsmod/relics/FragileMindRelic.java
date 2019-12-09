package stsjorbsmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.SnapAction;

// At the end of turn 7, Snap. Also act as a turn counter for QoL.
public class FragileMindRelic extends CustomJorbsModRelic {
    public static final String ID = JorbsMod.makeID(FragileMindRelic.class);

    public FragileMindRelic() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        ++this.counter;
        if (this.counter == 7) {
            this.beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter == 7) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new SnapAction(AbstractDungeon.player));
            this.stopPulse();
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
    }

    @Override
    public void initializeTips() {
        this.description = DESCRIPTIONS[0];
        super.initializeTips();
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replaceAll(JorbsMod.MOD_ID + ":", "#y");
    }
}
