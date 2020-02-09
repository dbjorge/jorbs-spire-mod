package stsjorbsmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.MakeMaterialComponentsInHandAction;

public class BookOfTonguesPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(BookOfTonguesPower.class);
    public static final String POWER_ID = STATIC.ID;
    private final boolean upgraded;

    public BookOfTonguesPower(final AbstractCreature owner, final int cardsPerTurn, final boolean upgraded) {
        super(STATIC);
        if (upgraded) {
            this.ID += "_upgraded"; // This ensures that the upgraded version stacks separately
            this.name += "+";
        }

        this.owner = owner;
        this.amount = cardsPerTurn;
        this.upgraded = upgraded;

        updateDescription();
    }

    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new MakeMaterialComponentsInHandAction(amount, upgraded));
        }
    }

    @Override
    public void updateDescription() {
        if (upgraded) {
            description = String.format(this.amount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3], this.amount);
        } else {
            description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], this.amount);
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BookOfTonguesPower(owner, amount, upgraded);
    }
}

