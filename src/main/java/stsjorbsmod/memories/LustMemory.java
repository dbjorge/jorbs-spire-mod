package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.powers.BurningPower;

public class LustMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(LustMemory.class);

    private static final int PASSIVE_BURNING_ON_ATTACK = 2;

    public LustMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.SIN, owner);
        setDescriptionPlaceholder("!P!", PASSIVE_BURNING_ON_ATTACK);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (isPassiveEffectActive()) {
            // Similar to EnvenomPower
            if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
                this.flash();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, this.owner, new BurningPower(target, this.owner, PASSIVE_BURNING_ON_ATTACK), PASSIVE_BURNING_ON_ATTACK, true));
            }
        }
    }
}
