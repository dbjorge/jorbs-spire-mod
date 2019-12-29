package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class HealOnFatalDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private int heal;
    private boolean worksOnMinions;

    public HealOnFatalDamageAction(AbstractCreature target, DamageInfo info, int healAmount) {
        this.info = info;
        this.setValues(target, info);
        this.heal = healAmount;
        this.actionType = ActionType.DAMAGE;
        this.worksOnMinions = true;
    }

    public HealOnFatalDamageAction(AbstractCreature target, DamageInfo info, int healAmount, boolean worksOnMinions) {
        this.info = info;
        this.setValues(target, info);
        this.heal = healAmount;
        this.actionType = ActionType.DAMAGE;
        this.worksOnMinions = worksOnMinions;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }
        this.tickDuration();
        if (this.isDone) {
            this.target.damage(this.info);
            if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead) {
                if (worksOnMinions || !this.target.hasPower(MinionPower.POWER_ID)) {
                    AbstractPlayer p = AbstractDungeon.player;
                    AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, heal));
                }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
    }
}