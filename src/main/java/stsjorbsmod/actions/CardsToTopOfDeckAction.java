package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;

// Based on DiscardPileToTopOfDeckAction
public class CardsToTopOfDeckAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(CardsToTopOfDeckAction.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p;
    private CardGroup sourcePile;

    // Note: this controls the order in which cards get placed on the top of the deck
    // Even if true, we remove cards from the sourcePile in-order (so players can't see the randomly chosen order)
    private boolean randomOrder;

    public CardsToTopOfDeckAction(AbstractCreature source, CardGroup sourcePile, int cardCount, boolean randomOrder) {
        this.p = AbstractDungeon.player;
        this.setValues((AbstractCreature)null, source, cardCount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;
        this.sourcePile = sourcePile;
        this.randomOrder = randomOrder;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            this.isDone = true;
        } else {
            CardGroup chosenCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            boolean isSourcePileHand = this.sourcePile == AbstractDungeon.player.hand;

            if (this.duration == Settings.ACTION_DUR_FASTER) {
                if (sourcePile.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                if (sourcePile.size() <= this.amount) {
                    for (AbstractCard c : sourcePile.group) {
                        chosenCards.addToTop(c);
                    }
                }

                if (sourcePile.size() > this.amount) {
                    // TEXT:
                    //      "Choose %1$s Card to move to the top of your draw pile.",
                    //      "Choose %1$s Cards to move to the top of your draw pile.",
                    //      "move to the top of your draw pile."
                    if (isSourcePileHand) {
                        String titleTextSuffix = TEXT[2];
                        AbstractDungeon.handCardSelectScreen.open(titleTextSuffix, this.amount, false);
                        sourcePile.applyPowers();
                    } else {
                        String titleText = String.format(this.amount == 1 ? TEXT[0] : TEXT[1], this.amount);
                        AbstractDungeon.gridSelectScreen.open(sourcePile, this.amount, titleText, false, false, false, false);
                    }
                    this.tickDuration();
                    return;
                }
            }

            ArrayList<AbstractCard> selectedCards = isSourcePileHand ?
                    AbstractDungeon.handCardSelectScreen.selectedCards.group :
                    AbstractDungeon.gridSelectScreen.selectedCards;

            if (!selectedCards.isEmpty() && (!isSourcePileHand || !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)) {
                for (AbstractCard c : selectedCards) {
                    chosenCards.addToTop(c);
                }
            }

            if (!chosenCards.isEmpty()) {
                if (this.randomOrder) {
                    chosenCards.shuffle(AbstractDungeon.cardRandomRng);
                }

                for (AbstractCard c : chosenCards.group) {
                    sourcePile.moveToDeck(c, false);
                }
                chosenCards.clear();

                if (!isSourcePileHand) {
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                }
                AbstractDungeon.player.hand.refreshHandLayout();
                if (isSourcePileHand) {
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                }
            }

            this.tickDuration();
        }
    }
}
