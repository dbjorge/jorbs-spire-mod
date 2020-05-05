package stsjorbsmod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.ExertedField;
import stsjorbsmod.patches.SelfExertField;

public class ExertAction extends AbstractGameAction{
    //private final CardGroup originalPile;
    private AbstractCard card;

    public ExertAction(AbstractCard card) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.card = card;
        //this.originalPile = originalPile;
    }

    @Override
    public void update() {
        AbstractCard masterCard;
        masterCard = StSLib.getMasterDeckEquivalent(this.card);
        JorbsMod.logger.info("Master Card: " + masterCard);
        JorbsMod.logger.info("masterDeck: " + AbstractDungeon.player.masterDeck);
        // I'm setting both masterdeck and combatdeck instances of the card to exerted,
        // in case we want to show text on exerted cards, or want to prevent exerted cards from being returned from Exhaust.
        if (masterCard != null) {
            //SelfExertField.selfExert.set(masterCard, true);
            ExertedField.exerted.set(masterCard, true);
            JorbsMod.logger.info("Set" + masterCard + "to exerted. Exerted? " + ExertedField.exerted.get(masterCard));
        }
        //SelfExertField.selfExert.set(this.card, true);
        ExertedField.exerted.set(this.card, true);
        JorbsMod.logger.info("Set" + this.card + "to exerted. Exerted? " + ExertedField.exerted.get(this.card));
        JorbsMod.logger.info("Finished ExertAction Update");
        isDone = true;
    }
}
