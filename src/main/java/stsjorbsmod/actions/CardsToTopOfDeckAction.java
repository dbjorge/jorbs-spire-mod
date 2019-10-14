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
import stsjorbsmod.powers.memories.AbstractMemoryPower;

// Based on DiscardPileToTopOfDeckAction
public class CardsToTopOfDeckAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(AbstractMemoryPower.class.getSimpleName());
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
            if (this.duration == Settings.ACTION_DUR_FASTER) {
                if (sourcePile.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                if (sourcePile.size() <= this.amount) {
                    for (AbstractCard c : sourcePile.group) {
                        chosenCards.addToBottom(c);
                        sourcePile.removeCard(c);
                    }
                    AbstractDungeon.player.hand.refreshHandLayout();
                }

                if (sourcePile.group.size() > this.amount) {
                    AbstractDungeon.gridSelectScreen.open(sourcePile, this.amount, TEXT[0], false, false, false, false);
                    this.tickDuration();
                    return;
                }
            }

            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    chosenCards.addToBottom(c);
                    sourcePile.removeCard(c);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            if (!chosenCards.isEmpty()) {
                if (this.randomOrder) {
                    chosenCards.shuffle(AbstractDungeon.cardRandomRng);
                }

                for (AbstractCard c : chosenCards.group) {
                    sourcePile.moveToDeck(c, false);
                }

                chosenCards.clear();
            }

            this.tickDuration();
        }
    }
}
