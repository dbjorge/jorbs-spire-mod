package stsjorbsmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import static stsjorbsmod.patches.EnumsPatch.SPECIAL;

public class OldBookPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(OldBookPower.class);
    public static final String POWER_ID = STATIC.ID;

    public static final String UPGRADED = "__UPGRADED";
    public static final String NORMAL = "__NORMAL";

    private static final String NAME = "Old Book";
    private static final String UPGRADED_NAME = "Old Book+";

    private AbstractCard card;

    public OldBookPower(final AbstractCreature owner, AbstractCard card, int amount) {
        super(STATIC);

        ID = POWER_ID + (card.upgraded ? UPGRADED : NORMAL);
        this.isTurnBased = false;
        this.card = card;
        this.owner = owner;
        this.amount = amount;
        this.type = SPECIAL;

        updateDescription();
    }

    public AbstractCard getCard() {
        return card;
    }

    @Override
    public void updateDescription() {
        if (card.upgraded) {
            name = UPGRADED_NAME;
            description = String.format(DESCRIPTIONS[1], card.magicNumber);
        }
        else {
            name = NAME;
            description = DESCRIPTIONS[0];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new OldBookPower(owner, card, amount);
    }
}
