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
        ArrayList<AbstractMemory> memories = new ArrayList<>();

        memories.add(new PatienceMemory(target));
        memories.add(new DiligenceMemory(target));
        memories.add(new ChastityMemory(target));
        memories.add(new KindnessMemory(target));
        memories.add(new HumilityMemory(target));
        memories.add(new CharityMemory(target));
        memories.add(new TemperanceMemory(target));

        memories.add(new LustMemory(target));
        memories.add(new EnvyMemory(target));
        memories.add(new WrathMemory(target));
        memories.add(new GreedMemory(target));
        memories.add(new GluttonyMemory(target));
        memories.add(new PrideMemory(target));
        memories.add(new SlothMemory(target));

        return memories;
    }

    public static boolean isValidMemoryID(String id) {
        return allPossibleMemoryIDs().stream().anyMatch(candidate -> candidate.equals(id));
    }
}
