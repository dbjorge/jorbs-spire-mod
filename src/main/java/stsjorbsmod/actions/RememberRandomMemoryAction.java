package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.powers.AbstractMemoryPower;
import stsjorbsmod.powers.DiligenceMemoryPower;
import stsjorbsmod.powers.PatienceMemoryPower;

import java.util.ArrayList;

// "Remember a random memory you do not have clarity of"
public class RememberRandomMemoryAction extends AbstractGameAction  {
    public RememberRandomMemoryAction(AbstractCreature target, AbstractCreature source) {
        this.setValues(target, source);
    }

    public void update() {
        ArrayList<AbstractMemoryPower> candidates = new ArrayList<>();
        candidates.add(new DiligenceMemoryPower(target, source));
        candidates.add(new PatienceMemoryPower(target, source));

        candidates.removeIf(memory -> target.hasPower(memory.ID));

        if (!candidates.isEmpty()) {
            int randomIndex = AbstractDungeon.cardRng.random.nextInt(candidates.size());
            AbstractMemoryPower chosenMemory = candidates.get(randomIndex);
            AbstractDungeon.actionManager.addToBottom(new RememberSpecificMemoryAction(target, source, chosenMemory));
        }

        isDone = true;
    }
}
