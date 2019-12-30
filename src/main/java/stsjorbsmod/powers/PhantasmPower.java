package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.JorbsMod;

public class PhantasmPower extends CustomJorbsModPower {
    public static final Logger logger = LogManager.getLogger(JorbsMod.class.getName());

    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PhantasmPower.class);
    public static final String POWER_ID = STATIC.ID;

    public PhantasmPower(AbstractCreature owner, int damageOnIntangible) {
        super(STATIC);

        this.owner = owner;
        this.amount = damageOnIntangible;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        logger.info("Intangible applied: ID: " + power.ID + " target: " + target + " source: " + source + "owner: " + power.owner);
        if (power.ID.equals(IntangiblePlayerPower.POWER_ID) && power.owner == AbstractDungeon.player) {
            logger.info("Phantasm triggered");
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON, true));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new PhantasmPower(owner, amount);
    }
}
