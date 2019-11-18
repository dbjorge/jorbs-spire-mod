package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnHealedBySubscriber {
    int onHealedBy(AbstractCreature source, int originalAmount);
}
