package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryUtils;

import java.util.ArrayList;
import java.util.List;

public class LoseRandomClarityAction extends AbstractGameAction {
    public LoseRandomClarityAction(AbstractCreature target) {
        this.setValues(target, target);
    }

    public void update() {
        MemoryManager memoryManager = MemoryManager.forPlayer(target);
        List<AbstractMemory> candidates = memoryManager.currentClarities();
        if (!candidates.isEmpty()) {
            int randomIndex = AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1);
            memoryManager.loseClarity(candidates.get(randomIndex));
        }

        isDone = true;
    }
}
