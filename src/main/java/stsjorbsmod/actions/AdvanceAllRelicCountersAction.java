package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AdvanceAllRelicCountersAction extends AbstractGameAction {
    AbstractPlayer player;

    public AdvanceAllRelicCountersAction(AbstractPlayer player, int relicCounterIncrement) {
        this.player = player;
        this.amount = relicCounterIncrement;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        for (AbstractRelic relic : player.relics) {
            if (relic.counter >= 0) {
                relic.setCounter(relic.counter + this.amount);
                relic.flash();
            }
        }

        isDone = true;
    }
}
