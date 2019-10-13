package stsjorbsmod.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.AbstractMemoryPower;

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
}
