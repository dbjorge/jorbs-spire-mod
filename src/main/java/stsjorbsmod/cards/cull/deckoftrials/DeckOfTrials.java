package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;

public class DeckOfTrials {
    public CardGroup deck;

    public DeckOfTrials() {
        deck = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    }

    public void reset() {
        deck.group.clear();
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.shuffle();
    }

    public ArrayList<AbstractCard> drawCards(int num) {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (!deck.isEmpty()) {
                AbstractCard card = deck.getTopCard();
                deck.removeCard(card);
                cards.add(card);
            }
        }
        return cards;
    }
}
