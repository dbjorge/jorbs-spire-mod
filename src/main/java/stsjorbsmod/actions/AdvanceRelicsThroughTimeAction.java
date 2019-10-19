package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AdvanceRelicsThroughTimeAction extends AbstractGameAction {
    AbstractPlayer player;

    public AdvanceRelicsThroughTimeAction(AbstractPlayer player, int relicCounterIncrement) {
        this.player = player;
        this.amount = relicCounterIncrement;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        for (AbstractRelic relic : player.relics) {
            if (relic.counter >= 0) {
                // We aren't just incrementing the counter directly because the relics generally aren't listening for
                // that to happen, so incrementing a counter to a relic's trigger value that way wouldn't trigger
                // the relic as desire. Instead, we simulate the passage of turn events, where those relics' trigger
                // behavior can get a chance to run.
                relic.atTurnStart();
                relic.atTurnStartPostDraw();
                relic.onPlayerEndTurn();
            }
        }

        isDone = true;
    }
}
