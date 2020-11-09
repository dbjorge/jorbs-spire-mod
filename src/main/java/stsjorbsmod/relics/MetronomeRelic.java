package stsjorbsmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.IncreaseManifestAction;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class MetronomeRelic extends CustomJorbsModIntStatsRelic {
    public static final String ID = JorbsMod.makeID(MetronomeRelic.class);

    public MetronomeRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onEquip(){this.counter = 0;}

    @Override
    public void atTurnStart(){
        this.counter++;
        if (this.counter == 5){
            this.counter = 0;
            flash();
            addToBot(new IncreaseManifestAction(-1, true));
        }
    }

}
