package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static stsjorbsmod.util.CardMetaUtils.downgradePermanently;
import static stsjorbsmod.util.CardMetaUtils.removeCard;
import static stsjorbsmod.util.EffectUtils.showDestroyEffect;
import static stsjorbsmod.util.EffectUtils.showDowngradeEffect;

public class PatronAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard card;

    public PatronAction(AbstractCreature target, DamageInfo info, AbstractCard card) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_MED;
        this.card = card;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            // order here matters. We want to downgrade after damage happens, but remove before damage to tag with purgeOnUse for Wrath
            if (card.upgraded) {
                this.target.damage(this.info);
                downgradePermanently(card, 0.25F);
            } else {
                removeCard(card);
                this.target.damage(this.info);
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        tickDuration();
        if (isDone) {
            if (card.upgraded) {
                showDowngradeEffect(card, duration);
            } else {
                showDestroyEffect(card);
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
    }
}
