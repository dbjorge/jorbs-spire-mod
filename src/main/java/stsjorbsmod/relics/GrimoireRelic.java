package stsjorbsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.PatienceMemory;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makeRelicOutlinePath;
import static stsjorbsmod.JorbsMod.makeRelicPath;

// Start each fight remembering Patience. At the end of each fight, gain 1hp per Clarity.
public class GrimoireRelic extends CustomRelic {
    private static final int HEAL_PER_CLARITY = 1;

    public static final String ID = JorbsMod.makeID(GrimoireRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("grimoire_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("grimoire_relic.png"));

    public GrimoireRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
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
            p.heal(MemoryManager.forPlayer(p).countCurrentClarities() * HEAL_PER_CLARITY);
        }
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
