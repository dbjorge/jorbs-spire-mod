package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

public class RediscoveryPower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(RediscoveryPower.class);
    public static final String POWER_ID = STATIC.ID;

    public RediscoveryPower(AbstractCreature owner) {
        super(STATIC);

        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.BUFF;

        this.isTurnBased = false;

        updateDescription();
    }

    @Override
    public void onGainClarity(String id) {
        flash();
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new RediscoveryPower(owner);
    }
}
