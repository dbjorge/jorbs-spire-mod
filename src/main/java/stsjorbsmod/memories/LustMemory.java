package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.powers.BurningPower;

public class LustMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(LustMemory.class);

    private static final int BURNING_ON_REMEMBER = 2;
    private static final int PASSIVE_BURNING_ON_ATTACK = 2;
    private static final int WEAK_ON_FORGET = 2;

    public LustMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.SIN, owner);
        setDescriptionPlaceholder("!R!", BURNING_ON_REMEMBER);
        setDescriptionPlaceholder("!P!", PASSIVE_BURNING_ON_ATTACK);
        setDescriptionPlaceholder("!F!", WEAK_ON_FORGET);
    }

    @Override
    public void onRemember() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (!monster.halfDead && !monster.isDead && !monster.isDying) {
                    // addToTop is important for the interaction with Inferno
                    AbstractDungeon.actionManager.addToTop(
                            new ApplyPowerAction(monster, owner, new BurningPower(monster, owner, BURNING_ON_REMEMBER), BURNING_ON_REMEMBER));
                }
            }
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (isPassiveEffectActive()) {
            // Similar to EnvenomPower
            if (target != this.owner && info.type == DamageInfo.DamageType.NORMAL) {
                this.flash();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, this.owner, new BurningPower(target, this.owner, this.PASSIVE_BURNING_ON_ATTACK), this.PASSIVE_BURNING_ON_ATTACK, true));
            }
        }
    }

    @Override
    public void onForget() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new WeakPower(owner, WEAK_ON_FORGET, false)));
    }
}
