package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.JorbsMod;

public class DecreaseMaxHpAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(DecreaseMaxHpAction.class);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    private static final String[] TEXT = uiStrings.TEXT;

    private final AttackEffect effect;

    public DecreaseMaxHpAction(AbstractCreature target, AbstractCreature source, int amount, AttackEffect effect) {
        this.effect = effect;
        this.amount = amount;
        this.target = target;
        this.source = source;

        // This has the side effect of making the decrease still happen if an associated effect just recently ended combat.
        this.actionType = ActionType.DAMAGE;

        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }
        this.duration = this.startDuration;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            String maxHpLossMessage = String.format(TEXT[0], amount);
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, maxHpLossMessage, Settings.RED_TEXT_COLOR));
            if (effect != AttackEffect.NONE) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, effect));
            }
        }

        this.tickDuration();

        if (isDone) {
            this.target.decreaseMaxHealth(amount);
        }
    }
}
