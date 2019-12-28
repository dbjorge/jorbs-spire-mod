package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.MakeMaterialComponentsInHandAction;

public class BookOfTonguesPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(BookOfTonguesPower.class);
    public static final String POWER_ID = STATIC.ID;

    public BookOfTonguesPower(final AbstractCreature owner, final int cardsPerTurn) {
        super(STATIC);

        this.owner = owner;
        this.amount = cardsPerTurn;

        updateDescription();
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new MakeMaterialComponentsInHandAction(this.amount));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new BookOfTonguesPower(owner, amount);
    }
}

