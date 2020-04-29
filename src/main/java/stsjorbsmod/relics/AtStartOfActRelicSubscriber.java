package stsjorbsmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public interface AtStartOfActRelicSubscriber {
    void atStartOfAct();

    static void doAtStartOfAct() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AtStartOfActRelicSubscriber) {
                ((AtStartOfActRelicSubscriber) r).atStartOfAct();
            }
        }
    }
}
