package stsjorbsmod.powers;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface CustomStackBehaviorPower {
    // *replaces* original stackPower behavior
    void stackPower(AbstractPower newInstanceOfSamePower);
}
