package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.cards.wanderer.materialcomponents.MaterialComponentsDeck;
import stsjorbsmod.patches.ExtraCopiesToAddWhenGeneratingCardField;

public class MakeMaterialComponentsInHandAction extends AbstractGameAction {
    public MakeMaterialComponentsInHandAction(int numberOfComponents) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = numberOfComponents;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; ++i) {
            AbstractCard newCard = MaterialComponentsDeck.drawRandomCard();
            int copiesToAdd = 1 + ExtraCopiesToAddWhenGeneratingCardField.field.get(newCard);
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(newCard, copiesToAdd, false));
        }
        isDone = true;
    }
}
