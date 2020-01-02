package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
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
            String msg = amount < 0 ?  String.format(TEXT[1], amount) : String.format(TEXT[0], amount);
            Color msgColor = amount < 0 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, msg, msgColor));
        }

        this.tickDuration();

        if (isDone) {
            ((Cull) target).manifest+= amount;
            if (((Cull) target).manifest < 0) {
                ((Cull) target).manifest = 0;
            }
        }
    }
}
