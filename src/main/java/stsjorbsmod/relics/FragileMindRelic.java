package stsjorbsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makeRelicOutlinePath;
import static stsjorbsmod.JorbsMod.makeRelicPath;

// At the end of turn 7, Snap. Also act as a turn counter for QoL.
public class FragileMindRelic extends CustomRelic {
    public static final String ID = JorbsMod.makeID(FragileMindRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("fragile_mind.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("fragile_mind.png"));

    public FragileMindRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
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
