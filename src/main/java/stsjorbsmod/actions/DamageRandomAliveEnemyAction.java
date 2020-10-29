package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DamageRandomAliveEnemyAction extends AbstractGameAction {
    private DamageInfo info;

    public DamageRandomAliveEnemyAction(AbstractCreature exclude, DamageInfo info) {
        this.info = info;
        this.source = exclude;
        this.actionType = ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    public void update() {
        if (AbstractDungeon.getMonsters() != null) {
            this.target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)this.source, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                addToTop(new DamageAction(target, new DamageInfo(this.source, this.source.maxHealth), AttackEffect.BLUNT_HEAVY));
                addToTop(new AnimateSlowAttackAction(this.source));
                if (Settings.FAST_MODE) {
                    addToTop(new WaitAction(0.1F));
                } else {
                    addToTop(new WaitAction(0.3F));
                }
            }
        }
        this.isDone = true;
    }
}
