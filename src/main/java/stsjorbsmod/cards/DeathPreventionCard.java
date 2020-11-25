package stsjorbsmod.cards;

import org.apache.commons.lang3.mutable.MutableInt;

public interface DeathPreventionCard {
    MutableInt currentPriority = new MutableInt(0);

    int getPriority();
}
