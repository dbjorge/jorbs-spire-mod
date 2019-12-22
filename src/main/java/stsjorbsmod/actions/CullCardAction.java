package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;
import java.util.UUID;

public class CullCardAction extends AbstractGameAction {
    private int increaseAmount;
    private DamageInfo info;
    private UUID uuid;
    private boolean first = false;
    private boolean second = false;

    public CullCardAction(AbstractCreature target, DamageInfo info, int incAmount, UUID targetUUID) {
        this.info = info;
        setValues(target, info);
        increaseAmount = incAmount;
        actionType = ActionType.DAMAGE;
        duration = startDuration = 0.2F;
        uuid = targetUUID;
    }

    public void update() {
        if (!first && duration == startDuration && target != null) {
            first = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.SLASH_HORIZONTAL));
            target.damage(this.info);
        } else if (!second && duration <= 0.1F && target != null) {
            second = true;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.SLASH_VERTICAL));
            target.damage(info);
            if ((target.isDying || target.currentHealth <= 0) && !target.halfDead) {
                for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c.uuid.equals(uuid)) {
                        c.misc += this.increaseAmount;
                        c.applyPowers();
                        c.baseDamage = c.misc;
                        c.isDamageModified = false;
                    }
                }

                for (AbstractCard c : GetAllInBattleInstances.get(uuid)) {
                    c.misc += this.increaseAmount;
                    c.applyPowers();
                    c.baseDamage = c.misc;
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }
}
