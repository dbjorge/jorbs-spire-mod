package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect;
import stsjorbsmod.powers.AbstractMemoryPower;

import java.util.Collections;

// This is like ApplyPowerAction, but with the additional effect of removing other non-clarified memories
public class RememberSpecificMemoryAction extends AbstractGameAction  {
    private AbstractMemoryPower memoryToRemember;
    private float startingDuration;

    public RememberSpecificMemoryAction(AbstractCreature target, AbstractCreature source, AbstractMemoryPower memoryToRemember, AttackEffect effect) {
        this.setValues(target, source, 1);
        this.memoryToRemember = memoryToRemember;

        this.actionType = ActionType.POWER;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;

        this.attackEffect = effect;
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.duration = 0.0F;
            this.startingDuration = 0.0F;
            this.isDone = true;
        }
    }

    public RememberSpecificMemoryAction(AbstractCreature target, AbstractCreature source, AbstractMemoryPower memoryToRemember) {
        this(target, source, memoryToRemember, AttackEffect.NONE);
    }

    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }

        if (this.duration == this.startingDuration) {
            if (this.target.hasPower(this.memoryToRemember.ID)) {
                this.isDone = true;
                return;
            }

            if (this.source != null) {
                for (AbstractPower power : this.source.powers) {
                    power.onApplyPower(this.memoryToRemember, this.target, this.source);
                }
            }

            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, this.memoryToRemember.name));

            for (AbstractPower oldPower : this.source.powers) {
                if (oldPower instanceof AbstractMemoryPower) {
                    AbstractMemoryPower oldMemory = (AbstractMemoryPower) oldPower;
                    if (!oldMemory.isClarified) {
                        AbstractDungeon.effectList.add(new PowerExpireTextEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, oldMemory.name, oldMemory.region128));
                        oldMemory.onRemove();
                        this.target.powers.remove(oldMemory);
                        AbstractDungeon.onModifyPower();
                    }
                }
            }

            this.target.powers.add(this.memoryToRemember);
            Collections.sort(this.target.powers);

            this.memoryToRemember.onInitialApplication();
            this.memoryToRemember.flash();

            AbstractDungeon.onModifyPower();
        }

        this.tickDuration();
    }
}
