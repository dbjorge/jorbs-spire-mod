package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.powers.BurningPower;

/**
 * As PoisonLoseHpAction, except:
 * - Decays as N -> floor(N/2) rather than poison's N -> N-1
 * - The "remove power" step happens as an end-of-round effect in BurningPower rather than as part of this action's
 * "reduce amount" step like poison does, because we want the "halve healing" effect to persist across the turn in
 * the burning amount is reduced to zero.
 */
public class BurningLoseHpAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(BurningLoseHpAction.class.getName());
    private static final float DURATION = 0.33F;

    public BurningLoseHpAction(AbstractCreature target, AbstractCreature source, int amount, AbstractGameAction.AttackEffect effect) {
        this.setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            if (this.duration == DURATION && this.target.currentHealth > 0) {
                logger.info(this.target.name + " HAS " + this.target.currentHealth + " HP.");
                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.ORANGE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS));
                }

                AbstractPower p = this.target.getPower(BurningPower.POWER_ID);
                if (p != null) {
                    p.amount /= 2;
                    p.updateDescription();
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
