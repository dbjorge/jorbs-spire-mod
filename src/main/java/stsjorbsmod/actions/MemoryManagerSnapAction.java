package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import stsjorbsmod.memories.MemoryManager;

public class MemoryManagerSnapAction extends AbstractGameAction {
    private MemoryManager manager;

    public MemoryManagerSnapAction(MemoryManager manager) {
        this.manager = manager;
    }

    @Override
    public void update() {
        this.manager.snap();
        this.isDone = true;
    }
}
