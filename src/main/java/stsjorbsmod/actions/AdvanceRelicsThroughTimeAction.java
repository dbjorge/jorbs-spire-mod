package stsjorbsmod.actions;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Arrays;
import java.util.function.Consumer;

// Notable test cases:
//   * Stone Calendar
//   * Happy Flower
//   * Ancient Tea Set
public class AdvanceRelicsThroughTimeAction extends AbstractTimeProgressionAction {
    private static final Runnable[] RELIC_TURN_PROGRESSION = new Runnable[] {
            () -> forEachRelic((r) -> r.onPlayerEndTurn()),
            () -> forEachRelic((r) -> r.atTurnStart()),
            () -> forEachRelic((r) -> r.atTurnStartPostDraw()),
    };

    private static void forEachRelic(Consumer<AbstractRelic> callback) {
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic.counter >= 0) {
                callback.accept(relic);
            }
        }
    }

    public AdvanceRelicsThroughTimeAction(AbstractPlayer player, int relicCounterIncrement) {
        super(Arrays.asList(RELIC_TURN_PROGRESSION), relicCounterIncrement);
    }
}
