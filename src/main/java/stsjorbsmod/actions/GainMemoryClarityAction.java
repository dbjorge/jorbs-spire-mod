package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.AbstractMemoryPower;


// "Gain clarity of your current memory"
public class GainMemoryClarityAction extends AbstractGameAction {
    public GainMemoryClarityAction(AbstractCreature target, AbstractCreature source) {
        this.setValues(target, source);
    }

    public void update() {
        for (AbstractPower oldPower : this.source.powers) {
            if (oldPower instanceof AbstractMemoryPower) {
                AbstractMemoryPower oldMemory = (AbstractMemoryPower) oldPower;
                if (!oldMemory.isClarified) {
                    oldMemory.flash();
                    oldMemory.isClarified = true;
                    oldMemory.updateDescription(); // for memories, also updates name to "Clarity of X"
                    AbstractDungeon.effectList.add(new TextAboveCreatureEffect(target.hb.cX, target.hb.cY, oldMemory.name, Color.WHITE));
                }
            }
        }

        isDone = true;
    }
}
