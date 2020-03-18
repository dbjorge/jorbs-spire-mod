package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import stsjorbsmod.relics.BookOfTrialsRelic;

import java.util.ArrayList;

public class DeckOfTrials {
    public static CardGroup deck = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public static void reset() {
        deck.group.clear();
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

    public static void addDeckOfTrialsCardsToMasterDeck() {
        if (AbstractDungeon.player.hasRelic(BookOfTrialsRelic.ID)) {
            AbstractDungeon.player.getRelic(BookOfTrialsRelic.ID).flash();
            ArrayList<AbstractCard> cards = DeckOfTrials.drawCards(2);
            for (AbstractCard card : cards) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            }
        }
    }
}
