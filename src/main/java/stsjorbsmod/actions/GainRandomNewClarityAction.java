package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryUtils;

import java.util.ArrayList;
import java.util.function.Predicate;

public class GainRandomNewClarityAction extends AbstractGameAction  {
    private Predicate<AbstractMemory> clarityFilter;

    public GainRandomNewClarityAction(AbstractCreature target, Predicate<AbstractMemory> clarityFilter) {
        this.setValues(target, target);
        this.clarityFilter = clarityFilter;
    }

    public void update() {
        ArrayList<AbstractMemory> candidates = MemoryUtils.allPossibleMemories(null);
        candidates.removeIf(memory -> !clarityFilter.test(memory));
        candidates.removeIf(memory -> MemoryManager.forPlayer(target).hasClarity(memory.ID));

        if (!candidates.isEmpty()) {
            int randomIndex = AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1);
            String chosenMemoryID = candidates.get(randomIndex).ID;
            AbstractDungeon.actionManager.addToTop(new GainSpecificClarityAction(target, chosenMemoryID));
        }

        isDone = true;
    }
}
