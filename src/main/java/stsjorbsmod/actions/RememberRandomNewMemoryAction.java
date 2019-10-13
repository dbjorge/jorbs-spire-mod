package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.powers.memories.*;

import java.util.ArrayList;

// "Remember a random memory you do not have clarity of"
public class RememberRandomNewMemoryAction extends AbstractGameAction  {
    private boolean isClarified;

    public RememberRandomNewMemoryAction(AbstractCreature target, AbstractCreature source, boolean isClarified) {
        this.setValues(target, source);
        this.isClarified = isClarified;
    }

    public void update() {
        ArrayList<AbstractMemoryPower> candidates = new ArrayList<>();

        candidates.add(new CharityMemoryPower(target, source, isClarified));
        candidates.add(new ChastityMemoryPower(target, source, isClarified));
        candidates.add(new DiligenceMemoryPower(target, source, isClarified));
        candidates.add(new EnvyMemoryPower(target, source, isClarified));
        candidates.add(new GreedMemoryPower(target, source, isClarified));
        candidates.add(new GluttonyMemoryPower(target, source, isClarified));
        candidates.add(new HumilityMemoryPower(target, source, isClarified));
        candidates.add(new KindnessMemoryPower(target, source, isClarified));
        candidates.add(new LustMemoryPower(target, source, isClarified));
        candidates.add(new PatienceMemoryPower(target, source, isClarified));
        candidates.add(new PrideMemoryPower(target, source, isClarified));
        candidates.add(new SlothMemoryPower(target, source, isClarified));
        candidates.add(new TemperanceMemoryPower(target, source, isClarified));
        // candidates.add(new WrathMemoryPower(target, source, isClarified));

        candidates.removeIf(memory -> target.hasPower(memory.ID));

        if (!candidates.isEmpty()) {
            int randomIndex = AbstractDungeon.cardRandomRng.random(0, candidates.size() - 1);
            AbstractMemoryPower chosenMemory = candidates.get(randomIndex);
            AbstractDungeon.actionManager.addToTop(new RememberSpecificMemoryAction(target, source, chosenMemory));
        }

        isDone = true;
    }
}
