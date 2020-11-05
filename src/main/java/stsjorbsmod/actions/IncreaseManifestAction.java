package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.ManifestPatch;
import stsjorbsmod.relics.MetronomeRelic;
import stsjorbsmod.toppanel.ManifestTopPanelItem;

public class IncreaseManifestAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(IncreaseManifestAction.class);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    private static final String[] TEXT = uiStrings.TEXT;
    private boolean fromMetronome;

    public IncreaseManifestAction(int amount) {
        this(amount, false);
    }

    public IncreaseManifestAction(int amount, boolean fromMetronome) {
        this.target = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.fromMetronome = fromMetronome;

        // This is for the case where a non-CULL character somehow gets a "gain manifest" card
        // Once such a card is played, the manifest icon will show until the end of the run
        ManifestTopPanelItem.show();
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            String msg = amount < 0 ?  String.format(TEXT[1], amount) : String.format(TEXT[0], amount);
            Color msgColor = amount < 0 ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR;
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, msg, msgColor));
        }

        this.tickDuration();

        if (isDone) {
            int originalManifest = ManifestPatch.PlayerManifestField.manifestField.get(target);
            int newManifest = Math.max(0, originalManifest + amount);
            if (fromMetronome) {
                MetronomeRelic relic = (MetronomeRelic)AbstractDungeon.player.getRelic(MetronomeRelic.ID);
                relic.addStats(originalManifest - newManifest);
            }
            ManifestPatch.PlayerManifestField.manifestField.set(target, newManifest);
        }
    }
}
