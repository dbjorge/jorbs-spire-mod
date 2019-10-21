package stsjorbsmod.memories;

import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.powers.IOnModifyGoldListener;

public class CharityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(CharityMemory.class);

    private static final int STRENGTH_PER_GOLD_THRESHOLD = 1;
    private static final int GOLD_THRESHOLD = 100;

    public CharityMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        setDescriptionPlaceholder("!S!", calculateBonusDamage());
        setDescriptionPlaceholder("!G!", GOLD_THRESHOLD);
    }

    private int calculateBonusDamage() {
        return (AbstractDungeon.player.gold / GOLD_THRESHOLD) * STRENGTH_PER_GOLD_THRESHOLD;
    }

    @Override
    public float atDamageGive(float originalDamage, DamageType type) {
        return isPassiveEffectActive && type == DamageType.NORMAL ?
                originalDamage + calculateBonusDamage() :
                originalDamage;
    }

    @Override
    public void onModifyGold(AbstractPlayer player) {
        setDescriptionPlaceholder("!S!", calculateBonusDamage());
        flashWithoutSound();
    }
}
