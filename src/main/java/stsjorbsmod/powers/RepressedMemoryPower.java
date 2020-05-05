package stsjorbsmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ExertAction;

public class RepressedMemoryPower extends CustomJorbsModPower{
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(CatharsisPower.class);
    public static final String POWER_ID = STATIC.ID;
    private final CardGroup originalPile;
    private final AbstractCard card;

    public RepressedMemoryPower(AbstractCreature owner, CardGroup originalPile, AbstractCard card) {
        super(STATIC);
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.originalPile = originalPile;
        this.card = card;
    }

    @Override
    public void onVictory() {
        addToBot(new ExertAction(originalPile, card));
    }

    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}
