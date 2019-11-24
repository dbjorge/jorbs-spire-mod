package stsjorbsmod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.patches.AutoExhumeField;

import java.util.function.Predicate;

public class ExhumeCardsAction extends AbstractGameAction {
    private static final float PADDING = 25.0F * Settings.scale;
    private final Predicate<AbstractCard> shouldApplyToCardPredicate;

    public ExhumeCardsAction(Predicate<AbstractCard> shouldApplyToCardPredicate) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.shouldApplyToCardPredicate = shouldApplyToCardPredicate;
    }

    public ExhumeCardsAction(AbstractCard specificCard) {
        this(c -> c == specificCard);
    }

    public ExhumeCardsAction(AutoExhumeBehavior targettedBehavior) {
        this(c -> AutoExhumeField.autoExhumeBehavior.get(c) == targettedBehavior);
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

        if (targetCards.size() > roomInHand) {
            p.createHandIsFullDialog();
            targetCards.shuffle();
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
        }

        this.isDone = true;
    }
}
