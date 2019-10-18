package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class CharityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(CharityMemory.class);

    private static final int STRENGTH_PER_GOLD_THRESHOLD = 1;
    private static final int GOLD_THRESHOLD = 100;

    private int strength;

    public CharityMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        setDescriptionPlaceholder("!S!", STRENGTH_PER_GOLD_THRESHOLD);
        setDescriptionPlaceholder("!G!", GOLD_THRESHOLD);
        this.strength = 0;
    }

    private int getStrengthAmount(int gold) {
        return Math.floorDiv(gold, GOLD_THRESHOLD) * STRENGTH_PER_GOLD_THRESHOLD;
    }

    // When remembered, and whenever something happens that might have changed the gold amount, recalculate strength gains.

    @Override
    protected void onRemember() {
        recalculateStrengthGain();
    }

    @Override
    public void atStartOfTurn() {
        recalculateStrengthGain();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        recalculateStrengthGain();
    }

    // If there's a change, apply it and flash the power.

    private void recalculateStrengthGain() {
        final int newStrength = getStrengthAmount(AbstractDungeon.player.gold);
        if (newStrength != this.strength) {
            this.flashWithoutSound();
            final int addAmount = newStrength - this.strength;
            final AbstractPlayer p = AbstractDungeon.player;
            this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, addAmount), addAmount));
            this.strength = newStrength;
        }
    }

    // Remove strength from the player when this memory is forgotten

    protected void onForget() {
        final int removeAmount = -this.strength;
        final AbstractPlayer p = AbstractDungeon.player;
        this.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, removeAmount), removeAmount));
    }

}
