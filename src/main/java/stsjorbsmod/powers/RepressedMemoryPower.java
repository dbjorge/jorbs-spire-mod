package stsjorbsmod.powers;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.ExertedField;
import stsjorbsmod.patches.SelfExertField;

import static stsjorbsmod.patches.EnumsPatch.SPECIAL;

public class RepressedMemoryPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(RepressedMemoryPower.class);
    public static final String POWER_ID = STATIC.ID;
    private AbstractCard card;

    public RepressedMemoryPower(AbstractPlayer owner,  AbstractCard card) {
        super(STATIC);
        this.ID = POWER_ID + card.uuid;
        this.owner = owner;
        this.type = SPECIAL;
        this.card = card;
    }

    @Override
    public void onVictory() {
        JorbsMod.logger.info("inside winning conditional");
        AbstractCard masterCard;
        masterCard = StSLib.getMasterDeckEquivalent(this.card);
        JorbsMod.logger.info("Master Card: " + masterCard);
        // I'm setting both masterdeck and combatdeck instances of the card to exerted,
        // in case we want to show text on exerted cards, or want to prevent exerted cards from being returned from Exhaust.
        if (masterCard != null) {
            SelfExertField.selfExert.set(masterCard, true);
            ExertedField.exerted.set(masterCard, true);
        }
        SelfExertField.selfExert.set(this.card, true);
        ExertedField.exerted.set(this.card, true);
    }

    @Override
    public AbstractPower makeCopy() {
        return null;
    }
}
