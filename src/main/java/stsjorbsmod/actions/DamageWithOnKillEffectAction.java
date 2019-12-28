package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.MinionPower;

public class DamageWithOnKillEffectAction extends AbstractGameAction {
    private DamageInfo info;
    private Runnable onKillCallback;
    private boolean worksOnMinions;


    public DamageWithOnKillEffectAction(AbstractCreature target, DamageInfo info, Runnable onKillCallback) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.onKillCallback = onKillCallback;
        this.worksOnMinions = false;
    }

    public DamageWithOnKillEffectAction(AbstractCreature target, DamageInfo info, Runnable onKillCallback, boolean worksOnMinions) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
        this.onKillCallback = onKillCallback;
        this.worksOnMinions = worksOnMinions;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {
                if (worksOnMinions || !this.target.hasPower(MinionPower.POWER_ID)) {
                    this.onKillCallback.run();
                }
            }
        }
        this.tickDuration();
    }
}
