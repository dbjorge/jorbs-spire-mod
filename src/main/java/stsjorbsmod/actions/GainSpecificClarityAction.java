package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import stsjorbsmod.memories.MemoryManager;

public class GainSpecificClarityAction extends AbstractGameAction  {
    private String memoryID;

    public GainSpecificClarityAction(AbstractCreature target, String memoryID) {
        this.setValues(target, target);
        this.memoryID = memoryID;
    }

    public void update() {
        MemoryManager.forPlayer(target).gainClarity(memoryID);
        isDone = true;
    }
}
