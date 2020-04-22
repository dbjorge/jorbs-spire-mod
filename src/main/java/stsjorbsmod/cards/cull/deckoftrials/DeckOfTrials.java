package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;

public class DeckOfTrials {
    public static CardGroup deck = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static void reset() {
        deck.group.clear();
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.addToTop(new Frostbite());
        deck.shuffle();
    }

    public static ArrayList<AbstractCard> drawCards(int num) {
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
