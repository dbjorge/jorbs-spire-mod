package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.relics.FragileMindRelic;

public class AdvanceRelicsThroughTimeAction extends AbstractGameAction {
    AbstractPlayer player;

    public AdvanceRelicsThroughTimeAction(AbstractPlayer player, int relicCounterIncrement) {
        this.player = player;
        this.amount = relicCounterIncrement;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; ++i) {
            for (AbstractRelic relic : player.relics) {
                if (!relic.relicId.equals(FragileMindRelic.ID) && relic.counter >= 0) {
                    // We aren't just incrementing the counter directly because the relics generally aren't listening for
                    // that to happen, so incrementing a counter to a relic's trigger value that way wouldn't trigger
                    // the relic as desire. Instead, we simulate the passage of the turn ending and then starting again,
                    // so those relics' trigger behaviors can get a chance to run.
                    //
                    // Notable test cases:
                    //   * Stone Calendar
                    //   * Happy Flower
                    //   * Fragile Mind (shouldn't be affected)
                    //   * Ancient Tea Set
                    relic.onPlayerEndTurn();
                    relic.atTurnStart();
                    relic.atTurnStartPostDraw();
                }
            }
        }

        isDone = true;
    }
}
