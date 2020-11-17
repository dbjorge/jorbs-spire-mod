package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ActionShouldPersistPostCombatField;

public class ReapAction extends AbstractGameAction {
    private AbstractCard card;

    public ReapAction(AbstractCard card, int amount) {
        this.card = card;
        this.amount = amount;
        ActionShouldPersistPostCombatField.shouldPersistPostCombat.set(this, true);
    }

    public void update() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (this.card.uuid == c.uuid) {
                c.misc += this.amount;
            }
        }
        this.isDone = true;
    }
}
