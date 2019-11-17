package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.cards.wanderer.SnakeOil;

/*
Material Components Deck:	Contains 7 common, 6 uncommon, 3 rare Material Components cards.
	At start of fight create deck of 7 commons, 2 uncommons, 1 rare drawn w/o replacement.
	If second deck is needed create a new one of 7/2/1 again. If a rarity has run out of cards to draw put all cards of that rarity back into the pool.
 	i.e. first deck is all 7 commons, 2 uncommons, 1 rare
	second deck will be all 7 commons again, 2 of the 4 other uncommons, 1 of the 2 other rares.
	resets completely for next fight.
 */
public class MaterialComponentsDeck {
    private static final CardGroup commonPool = new CardGroup(CardGroupType.UNSPECIFIED);
    private static final CardGroup uncommonPool = new CardGroup(CardGroupType.UNSPECIFIED);
    private static final CardGroup rarePool = new CardGroup(CardGroupType.UNSPECIFIED);

    private static final CardGroup deck = new CardGroup(CardGroupType.UNSPECIFIED);

    private static void resetCommonPool() {
        commonPool.clear();
        commonPool.addToTop(new Web());
        commonPool.addToTop(new SnakeOil());
        commonPool.addToTop(new Sulfur());
        commonPool.addToTop(new Eye());
        commonPool.addToTop(new Moss());
        commonPool.addToTop(new Steel());
        commonPool.addToTop(new Barb());
        commonPool.shuffle();
    }

    private static void resetUncommonPool() {
        uncommonPool.clear();
        uncommonPool.addToTop(new EnergyBulb());
        uncommonPool.addToTop(new LooseLeaf());
        uncommonPool.addToTop(new StrangePendant());
        // uncommonPool.addToTop(new Chamomile());
        uncommonPool.addToTop(new OakLeaf());
        // uncommonPool.addToTop(new Rot());
        uncommonPool.shuffle();
    }

    private static void resetRarePool() {
        rarePool.clear();
        rarePool.addToTop(new Quicksilver());
        // rarePool.addToTop(new TimeEddy());
        rarePool.addToTop(new Stone());
        rarePool.shuffle();
    }

    private static void drawNewDeckFromPools() {
        deck.clear();
        drawCardsFromPoolToDeck(7, commonPool, MaterialComponentsDeck::resetCommonPool);
        drawCardsFromPoolToDeck(2, uncommonPool, MaterialComponentsDeck::resetUncommonPool);
        drawCardsFromPoolToDeck(1, rarePool, MaterialComponentsDeck::resetRarePool);
        deck.shuffle();
    }

    private static void drawCardsFromPoolToDeck(int count, CardGroup pool, Runnable poolResetFunction) {
        for (int i = 0; i < count; ++i) {
            if (pool.isEmpty()) {
                poolResetFunction.run();
            }
            deck.addToTop(pool.getTopCard());
            pool.removeTopCard();
        }
    }

    public static void reset() {
        resetCommonPool();
        resetUncommonPool();
        resetRarePool();
        drawNewDeckFromPools();
    }

    public static AbstractCard drawRandomCard() {
        if (deck.isEmpty()) {
            drawNewDeckFromPools();
        }
        AbstractCard c = deck.getTopCard(); // drawNewDeck shuffled already, don't need to getRandomCard here
        deck.removeTopCard();
        return c;
    }
}
