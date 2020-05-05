package stsjorbsmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ExertAction;
import stsjorbsmod.patches.ExertedField;

public class RepressedMemoryPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(RepressedMemoryPower.class);
    public static final String POWER_ID = STATIC.ID;
    //private final CardGroup originalPile;
    private AbstractCard card;

    public RepressedMemoryPower(AbstractPlayer owner,  AbstractCard card) {
        super(STATIC);
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        //this.originalPile = originalPile;
        this.card = card;
    }

    @Override
    public void onVictory() {
        addToBot(new ExertAction(this.card));
        JorbsMod.logger.info("Did ExertAction on " + this.card + "Exerted? " + ExertedField.exerted.get(this.card));
    }

    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}
