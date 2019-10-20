package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

public class RememberSpecificMemoryAction extends AbstractGameAction  {
    private AbstractMemory memoryToRemember;

    public RememberSpecificMemoryAction(AbstractMemory memoryToRemember) {
        this.setValues(memoryToRemember.owner, memoryToRemember.owner);
        this.memoryToRemember = memoryToRemember;
    }

    public void update() {
        MemoryManager.forPlayer(target).rememberMemory(memoryToRemember);

        isDone = true;
    }
}
