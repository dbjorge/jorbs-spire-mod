package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import stsjorbsmod.util.ReflectionUtils;

import java.util.Iterator;
import java.util.UUID;

public class PermanentlyModifyDamageAction extends ModifyDamageAction {
    public PermanentlyModifyDamageAction(UUID targetUUID, int amount) {
        super(targetUUID, amount);
    }
    @Override
    public void update() {
        Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();
        UUID uuid = ReflectionUtils.getPrivateField(this, ModifyDamageAction.class, "uuid");

        AbstractCard c;
        while(var1.hasNext()) {
            c = (AbstractCard)var1.next();
            if (c.uuid.equals(uuid)) {
                c.misc += amount;
                c.applyPowers();
                c.baseDamage = c.misc;
                c.isDamageModified = false;
            }
        }

        for(var1 = GetAllInBattleInstances.get(uuid).iterator(); var1.hasNext(); c.baseDamage = c.misc) {
            c = (AbstractCard)var1.next();
            c.misc += this.amount;
            c.applyPowers();
        }
        super.update();
    }
}
