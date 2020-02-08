package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.patches.LegendaryPatch;

import java.util.function.Consumer;

public class PatronAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard card;
    private float baseDuration;
    private Consumer<AbstractCard> updateCard;
    private Consumer<AbstractCard> showEffect;

    public PatronAction(AbstractCreature target, DamageInfo info, AbstractCard card, Consumer<AbstractCard> updateCard, Consumer<AbstractCard> showEffect) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = baseDuration = Settings.ACTION_DUR_MED;
        this.card = card;
        this.updateCard = updateCard;
        this.showEffect = showEffect;
    }

    @Override
    public void update() {
        if (duration == baseDuration && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            updateCard.accept(card); // perform downgrade/destroy
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        tickDuration();
        if (isDone) {
            AbstractDungeon.actionManager.addToBottom(new ConsumerGameAction<>(showEffect, card));
            AbstractDungeon.actionManager.addToBottom(new ConsumerGameAction<>(LegendaryPatch::addCardToPools, card));
        }
    }
}
