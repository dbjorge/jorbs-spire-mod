package stsjorbsmod.memories;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.*;

import java.util.ArrayList;

public class MemoryUtils {
    public static ArrayList<AbstractMemory> getCurrentClarities(AbstractCreature creature) {
        ArrayList<AbstractMemory> retVal = new ArrayList<>();

        for (AbstractPower power : creature.powers) {
            if (power instanceof AbstractMemory) {
                AbstractMemory memory = (AbstractMemory) power;
                if (memory.isClarified) { retVal.add(memory); }
            }
        }

        return retVal;
    }

    public static int countClarities(AbstractCreature creature) {
        return getCurrentClarities(creature).size();
    }

    public static AbstractMemory getCurrentMemory(AbstractCreature creature) {
        for (AbstractPower power : creature.powers) {
            if (power instanceof AbstractMemory) {
                AbstractMemory memory = (AbstractMemory) power;
                if (!memory.isClarified) { return memory; }
            }
        }
        return null;
    }

    public static ArrayList<String> allActiveMemoryIDs(AbstractCreature creature) {
        ArrayList<String> ids = new ArrayList<>();
        for (AbstractPower p : creature.powers) {
            if (p instanceof AbstractMemory) {
                ids.add(p.ID);
            }
        }
        return ids;
    }

    public static ArrayList<String> allPossibleMemoryIDs() {
        ArrayList<String> ids = new ArrayList<>();
        for (AbstractMemory p : allPossibleMemorys(null, false)) {
            ids.add(p.ID);
        }
        return ids;
    }

    public static ArrayList<AbstractMemory> allPossibleMemorys(AbstractCreature target, boolean isClarified) {
        ArrayList<AbstractMemory> powers = new ArrayList<>();

        powers.add(new CharityMemory(target, isClarified));
        powers.add(new ChastityMemory(target, isClarified));
        powers.add(new DiligenceMemory(target, isClarified));
        powers.add(new EnvyMemory(target, isClarified));
        powers.add(new GreedMemory(target, isClarified));
        powers.add(new GluttonyMemory(target, isClarified));
        powers.add(new HumilityMemory(target, isClarified));
        powers.add(new KindnessMemory(target, isClarified));
        powers.add(new LustMemory(target, isClarified));
        powers.add(new PatienceMemory(target, isClarified));
        powers.add(new PrideMemory(target, isClarified));
        powers.add(new SlothMemory(target, isClarified));
        powers.add(new TemperanceMemory(target, isClarified));
        powers.add(new WrathMemory(target, isClarified));

        return powers;
    }

    public static AbstractMemory newMemoryByID(String id, AbstractCreature target, boolean isClarified) {
        for (AbstractMemory memory : allPossibleMemorys(target, isClarified)) {
            if (memory.ID.equals(id)) {
                return memory;
            }
        }
        throw new RuntimeException("MemoryUtils.newMemoryByID was passed an unrecognized ID");
    }
}
