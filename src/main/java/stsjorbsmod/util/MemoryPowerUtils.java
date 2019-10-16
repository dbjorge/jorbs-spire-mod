package stsjorbsmod.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.memories.*;

import java.util.ArrayList;

public class MemoryPowerUtils {
    public static int countClarities(AbstractCreature creature) {
        int count = 0;

        for (AbstractPower power : creature.powers) {
            if (power instanceof AbstractMemoryPower) {
                AbstractMemoryPower memory = (AbstractMemoryPower) power;
                if (memory.isClarified) { ++count; }
            }
        }

        return count;
    }

    public static AbstractMemoryPower getCurrentMemory(AbstractCreature creature) {
        for (AbstractPower power : creature.powers) {
            if (power instanceof AbstractMemoryPower) {
                AbstractMemoryPower memory = (AbstractMemoryPower) power;
                if (!memory.isClarified) { return memory; }
            }
        }
        return null;
    }

    public static ArrayList<String> allActiveMemoryIDs(AbstractCreature creature) {
        ArrayList<String> ids = new ArrayList<>();
        for (AbstractPower p : creature.powers) {
            if (p instanceof AbstractMemoryPower) {
                ids.add(p.ID);
            }
        }
        return ids;
    }

    public static ArrayList<String> allPossibleMemoryIDs() {
        ArrayList<String> ids = new ArrayList<>();
        for (AbstractMemoryPower p : allPossibleMemoryPowers(null, false)) {
            ids.add(p.ID);
        }
        return ids;
    }

    public static ArrayList<AbstractMemoryPower> allPossibleMemoryPowers(AbstractCreature target, boolean isClarified) {
        ArrayList<AbstractMemoryPower> powers = new ArrayList<>();

        powers.add(new CharityMemoryPower(target, isClarified));
        powers.add(new ChastityMemoryPower(target, isClarified));
        powers.add(new DiligenceMemoryPower(target, isClarified));
        powers.add(new EnvyMemoryPower(target, isClarified));
        powers.add(new GreedMemoryPower(target, isClarified));
        powers.add(new GluttonyMemoryPower(target, isClarified));
        powers.add(new HumilityMemoryPower(target, isClarified));
        powers.add(new KindnessMemoryPower(target, isClarified));
        powers.add(new LustMemoryPower(target, isClarified));
        powers.add(new PatienceMemoryPower(target, isClarified));
        powers.add(new PrideMemoryPower(target, isClarified));
        powers.add(new SlothMemoryPower(target, isClarified));
        powers.add(new TemperanceMemoryPower(target, isClarified));
        powers.add(new WrathMemoryPower(target, isClarified));

        return powers;
    }

    public static AbstractMemoryPower newMemoryByID(String id, AbstractCreature target, boolean isClarified) {
        for (AbstractMemoryPower memory : allPossibleMemoryPowers(target, isClarified)) {
            if (memory.ID.equals(id)) {
                return memory;
            }
        }
        throw new RuntimeException("MemoryPowerUtils.newMemoryByID was passed an unrecognized ID");
    }
}
