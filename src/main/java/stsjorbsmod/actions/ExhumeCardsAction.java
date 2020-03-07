package stsjorbsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import stsjorbsmod.cards.OnCardExhumedSubscriber;
import stsjorbsmod.cards.wanderer.Grimoire;

import java.util.Comparator;
import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class ExhumeCardsAction extends AbstractGameAction {
    private static final float PADDING = 25.0F * Settings.scale;
    private final Predicate<AbstractCard> shouldApplyToCardPredicate;

    public ExhumeCardsAction(Predicate<AbstractCard> shouldApplyToCardPredicate) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.shouldApplyToCardPredicate = shouldApplyToCardPredicate;
    }

    public ExhumeCardsAction(AbstractCard specificCard) {
        this(c -> c.uuid.equals(specificCard.uuid));
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        int roomInHand = BaseMod.MAX_HAND_SIZE - p.hand.size();

        CardGroup targetCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.exhaustPile.group) {
            if (shouldApplyToCardPredicate.test(c)) {
                targetCards.addToBottom(c);
            }
        }

        // Order is "grimoire first, then other legendaries, then by rarer-cards-first, then random"
        targetCards.shuffle();
        targetCards.sortByRarity(false);
        targetCards.group.sort(new CardsMatchingPredicateFirstComparator(c -> c.hasTag(LEGENDARY)));
        targetCards.group.sort(new CardsMatchingPredicateFirstComparator(c -> c.cardID.equals(Grimoire.ID)));

        if (targetCards.size() > roomInHand) {
            p.createHandIsFullDialog();
        }

        for (AbstractCard c : targetCards.group) {
            c.unfadeOut();
            if (p.hand.size() < BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
            }

            // Special case per ExhumeAction
            if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && c.type == AbstractCard.CardType.SKILL) {
                c.setCostForTurn(-9);
            }

            p.exhaustPile.removeCard(c);

            c.unhover();
            c.fadingOut = false;

            if (c instanceof OnCardExhumedSubscriber) {
                ((OnCardExhumedSubscriber) c).onCardExhumed();
            }
        }

        this.isDone = true;
    }

    private class CardsMatchingPredicateFirstComparator implements Comparator<AbstractCard> {
        private final Predicate<AbstractCard> predicate;
        public CardsMatchingPredicateFirstComparator(Predicate<AbstractCard> predicate) {
            this.predicate = predicate;
        }
        public int compare(AbstractCard c1, AbstractCard c2) {
            return Boolean.compare(predicate.test(c2), predicate.test(c1));
        }
    }
}
