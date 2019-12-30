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
import stsjorbsmod.characters.Cull;

public class IncreaseManifestAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(IncreaseManifestAction.class);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    private static final String[] TEXT = uiStrings.TEXT;

    public IncreaseManifestAction(int amount) {
        this.target = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (!(target instanceof Cull)) {
            JorbsMod.logger.warn("Ignoring attempt to add manifest to non-Cull character!");
            isDone = true;
            return;
        }

        Cull player = (Cull) target;

        if (this.duration == this.startDuration) {
            String msg = String.format(TEXT[0], amount);
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, msg, Settings.RED_TEXT_COLOR));
        }

        this.tickDuration();

        if (isDone) {
            ((Cull) target).manifest++;
        }
    }
}
