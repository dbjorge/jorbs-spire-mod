package stsjorbsmod.memories;

import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.cards.wanderer.Thorns;
import stsjorbsmod.util.ReflectionUtils;

public class HumilityMemory extends AbstractMemory {
    public static final Logger logger = LogManager.getLogger(HumilityMemory.class.getName());
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(HumilityMemory.class);

    private static final int THORNS_ON_REMEMBER = 2;
    private static final int THORNS_PASSIVE = 2;

    public HumilityMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);

        setDescriptionPlaceholder("!R!", THORNS_ON_REMEMBER);
        setDescriptionPlaceholder("!P!", THORNS_PASSIVE);
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new ThornsPower(owner, THORNS_ON_REMEMBER)));
    }

    @Override
    public void onGainPassiveEffect() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new ThornsPower(owner, THORNS_PASSIVE)));
    }

    @Override
    public void onLosePassiveEffect() {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ThornsPower.POWER_ID, THORNS_PASSIVE));
    }
}
