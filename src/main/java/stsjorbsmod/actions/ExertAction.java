package stsjorbsmod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import stsjorbsmod.patches.ExertedField;

public class ExertAction extends AbstractGameAction{
    private final CardGroup originalPile;
    private final AbstractCard card;

    public ExertAction(CardGroup originalPile, AbstractCard card) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.card = card;
        this.originalPile = originalPile;
    }

    @Override
    public void update() {
        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);

        // I'm setting both masterdeck and combatdeck instances of the card to exerted,
        // in case we want to show text on exerted cards, or want to prevent exerted cards from being returned from Exhaust.
        if (masterCard != null) {
            ExertedField.exerted.set(masterCard, true);
        }
        card.exhaust = true;
        originalPile.removeCard(this.card);
        isDone = true;
    }
}
