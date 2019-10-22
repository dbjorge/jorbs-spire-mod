package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;
import java.util.function.Predicate;

// Based on ExhaustSpecificCardAction
public class ExhaustCardsMatchingPredicateAction extends AbstractGameAction {
    private CardGroup pile;
    private Predicate<AbstractCard> filter;

    public ExhaustCardsMatchingPredicateAction(
            AbstractCreature source, CardGroup pile, Predicate<AbstractCard> filter)
    {
        this.setValues(AbstractDungeon.player, source, 1);
        this.actionType = ActionType.EXHAUST;
        this.duration = Settings.ACTION_DUR_FASTER;
        this.pile = pile;
        this.filter = filter;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> toExhaust = new ArrayList<AbstractCard>();
        for (AbstractCard c : pile.group) {
            if (filter.test(c)) {
                toExhaust.add(c);
            }
        }

        for (AbstractCard c : toExhaust) {
            this.pile.moveToExhaustPile(c);
            CardCrawlGame.dungeon.checkForPactAchievement();
            c.exhaustOnUseOnce = false;
            c.freeToPlayOnce = false;
        }

        this.tickDuration();
    }
}
