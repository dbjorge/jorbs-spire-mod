package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.characters.ReapAndSowSaveData;


public class ReapAndSowPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ReapAndSowPower.class);
    public static final String POWER_ID = STATIC.ID;

    public ReapAndSowPower(AbstractCreature owner, int amount) {
        super(STATIC);
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = amount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ReapAndSowPower(owner, amount);
    }

    @Override
    public void onVictory() {
        super.onVictory();
        ReapAndSowSaveData.reapAndSowDamage = this.amount;
    }
}
