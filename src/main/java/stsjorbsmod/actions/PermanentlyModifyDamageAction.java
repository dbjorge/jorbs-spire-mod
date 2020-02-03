package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import stsjorbsmod.patches.ActionShouldPersistPostCombatField;

import java.util.UUID;

public class PermanentlyModifyDamageAction extends AbstractGameAction {
    private UUID uuid;

    public PermanentlyModifyDamageAction(UUID targetUUID, int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
        ActionShouldPersistPostCombatField.shouldPersistPostCombat.set(this, true);
    }

    @Override
    public void update() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(uuid)) {
                c.baseDamage += this.amount;
                if (c.baseDamage < 0) {
                    c.baseDamage = 0;
                }
                c.misc += this.amount;
                if (c.misc < 0) {
                    c.misc = 0;
                }
            }
        }

        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
            c.baseDamage += this.amount;
            if (c.baseDamage < 0) {
                c.baseDamage = 0;
            }
            c.misc += this.amount;
            if (c.misc < 0) {
                c.misc = 0;
            }
        }

        this.isDone = true;
    }
}
