package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DamageWithOnKillEffectAction extends AbstractGameAction {
    
    private DamageInfo info;
    private Runnable onKillCallback;
    private boolean worksOnMinions;


    public DamageWithOnKillEffectAction(AbstractCreature target, DamageInfo info, Runnable onKillCallback) {
        this(target, info, onKillCallback, false);
    }

    public DamageWithOnKillEffectAction(AbstractCreature target, DamageInfo info, Runnable onKillCallback, boolean worksOnMinions) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.onKillCallback = onKillCallback;
        this.worksOnMinions = worksOnMinions;
    }

    private boolean isDying(AbstractCreature c) {
        return (c.isDying || c.currentHealth <= 0) && !c.halfDead;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }
        this.tickDuration();
        if (this.isDone) {
            boolean wasDyingBeforeDamage = isDying(this.target);
            this.target.damage(this.info);
            if (!wasDyingBeforeDamage && isDying(this.target)) {
                if (worksOnMinions || !this.target.hasPower(MinionPower.POWER_ID)) {
                    this.onKillCallback.run();
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}
