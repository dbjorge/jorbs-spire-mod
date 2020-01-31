package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ConsumeRandomCardAction extends AbstractGameAction {
    private final CardGroup pileToConsumeFrom;

    public ConsumeRandomCardAction(CardGroup pileToConsumeFrom) {
        this.pileToConsumeFrom = pileToConsumeFrom;
        actionType = ActionType.DAMAGE; // ensures it won't be cleared at the end of combat
    }

    public void update() {
        CardGroup candidateCards = CardGroup.getGroupWithoutBottledCards(pileToConsumeFrom.getPurgeableCards());
        if (!candidateCards.isEmpty()) {
            AbstractCard randomlyChosenCard = candidateCards.getRandomCard(AbstractDungeon.cardRandomRng);
            addToTop(new ConsumeCardAction(randomlyChosenCard));
        }
        isDone = true;
    }
}
