package stsjorbsmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.PatienceMemory;

import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

// Start each fight remembering Patience. At the end of each fight, gain 1hp per Clarity.
public class GrimoireRelic extends CustomJorbsModIntStatsRelic {
    public static final String ID = JorbsMod.makeID(GrimoireRelic.class);

    private static final int HEAL_PER_CLARITY = 1;

    public GrimoireRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
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
            int previousHealth = p.currentHealth;
            p.heal(MemoryManager.forPlayer(p).countCurrentClarities() * HEAL_PER_CLARITY);
            addStats(p.currentHealth - previousHealth);
        }
    }
}
