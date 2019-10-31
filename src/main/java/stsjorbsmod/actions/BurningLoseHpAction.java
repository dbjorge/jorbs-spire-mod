package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.powers.BurningPower;

/**
 * Burning Action:
 * - Deal N damage
 * - Reduce N to floor(N/2)
 */
public class BurningLoseHpAction extends AbstractGameAction{
    private static final Logger logger = LogManager.getLogger(BurningLoseHpAction.class.getName());
    private static final float DURATION = 0.33F;

    public BurningLoseHpAction(AbstractCreature target, AbstractCreature source, int amount, AbstractGameAction.AttackEffect effect) {
        this.setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.33F;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            if (this.duration == 0.33F && this.target.currentHealth > 0) {
                logger.info(this.target.name + " HAS " + this.target.currentHealth + " HP.");
                this.target.damageFlash = true;
                this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.CHARTREUSE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    this.target.damage(new DamageInfo(this.source, this.amount, DamageInfo.DamageType.HP_LOSS));
                    if (this.target.isDying) {
                        // TODO find out what poisonKillCount's purpose. Perhaps we want something similar
                        // ++AbstractPlayer.poisonKillCount;
                    }
                }

                AbstractPower p = this.target.getPower(BurningPower.POWER_ID);
                if (p != null) {
                    p.amount/=2;
                    if (p.amount == 0) {
                        this.target.powers.remove(p);
                    } else {
                        p.updateDescription();
                    }
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
