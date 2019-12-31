package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class PrestidigitationPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PrestidigitationPower.class);
    public static final String POWER_ID = STATIC.ID;

    public AbstractCard sourceCard;

    public PrestidigitationPower(final AbstractCreature owner, final int amount) {
        super(STATIC);

        this.owner = owner;
        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        AbstractCreature target = AbstractDungeon.getRandomMonster();
        if (target != null) {
            this.flash();
            AbstractPower effect = AbstractDungeon.cardRandomRng.randomBoolean() ?
                    new WeakPower(target, amount, !owner.isPlayer) :
                    new VulnerablePower(target, amount, !owner.isPlayer);

            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, effect, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], this.amount, this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new PrestidigitationPower(owner, amount);
    }
}

