package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.*;
import stsjorbsmod.memories.MemoryUtils;

import java.util.ArrayList;

// "Remember a random memory you do not have clarity of"
public class RememberRandomNewMemoryAction extends AbstractGameAction  {

    public RememberRandomNewMemoryAction(AbstractCreature target) {
        this.setValues(target, target);
    }

    public void update() {
        ArrayList<String> candidates = MemoryUtils.allPossibleMemoryIDs();
        candidates.removeIf(memory -> MemoryManager.forPlayer(target).hasMemoryOrClarity(memory));

        if (!candidates.isEmpty()) {
            int randomIndex = AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1);
            String chosenMemoryID = candidates.get(randomIndex);
            AbstractDungeon.actionManager.addToTop(new RememberSpecificMemoryAction(target, chosenMemoryID));
        }

        isDone = true;
    }
}
