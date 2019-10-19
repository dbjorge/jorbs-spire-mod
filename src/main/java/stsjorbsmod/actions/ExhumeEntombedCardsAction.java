package stsjorbsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import stsjorbsmod.patches.EntombedField;

public class ExhumeEntombedCardsAction extends AbstractGameAction {
    public ExhumeEntombedCardsAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int roomInHand = BaseMod.MAX_HAND_SIZE - p.hand.size();

        if (roomInHand == 0) {
            p.createHandIsFullDialog();
            isDone = true;
            return;
        }

        CardGroup targetCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.exhaustPile.group) {
            if (EntombedField.entombed.get(c)) {
                targetCards.addToBottom(c);
            }
        }

        if (targetCards.size() > roomInHand) {
            targetCards.shuffle();
            for (int i=targetCards.size(); i > roomInHand; --i) {
                targetCards.removeTopCard();
            }
        }

        for (AbstractCard c : targetCards.group) {
            // This loop body taken from ExhumeAction
            c.unfadeOut();
            p.hand.addToHand(c);
            if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && c.type == AbstractCard.CardType.SKILL) {
                c.setCostForTurn(-9);
            }

            p.exhaustPile.removeCard(c);

            c.unhover();
            c.fadingOut = false;
        }

        this.isDone = true;
    }
}
