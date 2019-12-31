package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.patches.EnumsPatch;

// Cannot be affected by memories or clarities.
// Marker power only; MemoryManager looks for it
public class SnappedPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(SnappedPower.class);
    public static final String POWER_ID = STATIC.ID;

    public SnappedPower(final AbstractCreature owner) {
        super(STATIC);

        this.owner = owner;

        type = EnumsPatch.SPECIAL;
        isTurnBased = false;
    }

    @Override
    public void onInitialApplication() {
        // Most of the "on snapped" effects get queued in SnapAction

        // This is for the benefit of Mindworm
        AbstractDungeon.player.hand.glowCheck();
    }

    @Override
    public AbstractPower makeCopy() {
        return new SnappedPower(owner);
    }
}
