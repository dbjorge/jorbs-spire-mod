package stsjorbsmod.cards.cull.deckoftrials;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static stsjorbsmod.characters.Cull.Enums.CULL;

public class DeckOfTrials implements CustomSavable<String> {
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

    @Override
    public String onSave() {
        return String.join(",", deck.group.stream().map(c -> c.cardID).collect(Collectors.toList()));
    }

    @Override
    public void onLoad(String deckOfTrials) {
        deck = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Arrays.asList(deckOfTrials.split(",")).forEach(c -> deck.addToTop(CardLibrary.getCard(CULL, c)));
    }
}
