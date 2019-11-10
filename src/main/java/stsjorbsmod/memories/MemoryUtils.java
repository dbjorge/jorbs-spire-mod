package stsjorbsmod.memories;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class MemoryUtils {
    public static ArrayList<String> allPossibleMemoryIDs() {
        ArrayList<String> ids = new ArrayList<>();
        for (AbstractMemory p : allPossibleMemories(null)) {
            ids.add(p.ID);
        }
        return ids;
    }

    public static ArrayList<AbstractMemory> allPossibleMemories(AbstractCreature target) {
        ArrayList<AbstractMemory> powers = new ArrayList<>();

        powers.add(new PatienceMemory(target));
        powers.add(new DiligenceMemory(target));
        powers.add(new ChastityMemory(target));
        powers.add(new KindnessMemory(target));
        powers.add(new HumilityMemory(target));
        powers.add(new CharityMemory(target));
        powers.add(new TemperanceMemory(target));

        powers.add(new LustMemory(target));
        powers.add(new EnvyMemory(target));
        powers.add(new WrathMemory(target));
        powers.add(new GreedMemory(target));
        powers.add(new GluttonyMemory(target));
        powers.add(new PrideMemory(target));
        powers.add(new SlothMemory(target));

        return powers;
    }

    public static AbstractMemory newMemoryByID(String id, AbstractCreature target, boolean isClarified) {
        for (AbstractMemory memory : allPossibleMemories(target)) {
            if (memory.ID.equals(id)) {
                return memory;
            }
        }
        return null;
    }
}
