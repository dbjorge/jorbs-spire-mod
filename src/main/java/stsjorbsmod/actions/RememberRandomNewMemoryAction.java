package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.*;
import stsjorbsmod.memories.MemoryUtils;

import java.util.ArrayList;

// "Remember a random memory you do not have clarity of"
public class RememberRandomNewMemoryAction extends AbstractGameAction  {
    private boolean isClarified;

    public RememberRandomNewMemoryAction(AbstractCreature target, AbstractCreature source, boolean isClarified) {
        this.setValues(target, source);
        this.isClarified = isClarified;
    }

    public void update() {
        ArrayList<AbstractMemory> candidates = MemoryUtils.allPossibleMemories(target, isClarified);

        candidates.removeIf(memory -> target.hasPower(memory.ID));

        if (!candidates.isEmpty()) {
            int randomIndex = AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1);
            AbstractMemory chosenMemory = candidates.get(randomIndex);
            AbstractDungeon.actionManager.addToTop(new RememberSpecificMemoryAction(chosenMemory));
        }

        isDone = true;
    }
}
