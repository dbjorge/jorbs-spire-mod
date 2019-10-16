package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryUtils;


// "Gain clarity of your current memory"
public class GainMemoryClarityAction extends AbstractGameAction {
    private String specificMemoryID;

    public GainMemoryClarityAction(AbstractCreature owner) {
        this.setValues(owner, owner);
    }

    public GainMemoryClarityAction(AbstractCreature owner, String specificMemoryID) {
        this.setValues(owner, owner);
        this.specificMemoryID = specificMemoryID;
    }

    public void update() {
        AbstractMemory oldMemory = MemoryUtils.getCurrentMemory(this.target);
        if (oldMemory != null && !oldMemory.isClarified && (specificMemoryID == null || specificMemoryID == oldMemory.ID)) {
            oldMemory.flash();
            oldMemory.isClarified = true;
            oldMemory.updateDescription(); // for memories, also updates name to "Clarity of X"
            AbstractDungeon.effectList.add(new TextAboveCreatureEffect(target.hb.cX, target.hb.cY, oldMemory.name, Color.WHITE));
            AbstractDungeon.onModifyPower();
        }

        isDone = true;
    }
}
