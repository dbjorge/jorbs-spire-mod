package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class PermanentlyModifyDamageAction extends AbstractGameAction {
    private UUID uuid;

    public PermanentlyModifyDamageAction(UUID targetUUID, int amount) {
        setValues(target, source, amount);
        actionType = ActionType.CARD_MANIPULATION;
        uuid = targetUUID;
    }

    @Override
    public void update() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(uuid)) {
                c.misc += amount;
                if (c.misc < 0) {
                    c.misc = 0;
                }
                c.applyPowers();
                c.baseDamage = c.misc;
                c.isDamageModified = false;
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
