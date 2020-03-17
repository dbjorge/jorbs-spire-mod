package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WitherPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(WitherPower.class);
    public static final String POWER_ID = STATIC.ID;
    public WitherPower(AbstractCreature owner) {
        super(STATIC);
        this.amount = -1;
        this.updateDescription();
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.img = this.region48.getTexture();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
    @Override
    public AbstractPower makeCopy() {
        return new WitherPower(owner);
    }
}
