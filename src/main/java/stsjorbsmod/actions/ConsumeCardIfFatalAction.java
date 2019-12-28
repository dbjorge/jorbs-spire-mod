package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.MinionPower;

public class ConsumeCardIfFatalAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard card;

    public ConsumeCardIfFatalAction(AbstractCreature target, DamageInfo info, AbstractCard c) {
        this.info = info;
        this.setValues(target, info);
        this.card = c;
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower(MinionPower.POWER_ID)) {
                addToBot(new ConsumeCardAction(this.card));
            }
        }
        this.tickDuration();
    }
}
