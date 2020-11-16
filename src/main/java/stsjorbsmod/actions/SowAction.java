package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.ActionShouldPersistPostCombatField;

public class SowAction extends AbstractGameAction {
    public SowAction(int sowDamage) {
        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(sowDamage, true),
                DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        ActionShouldPersistPostCombatField.shouldPersistPostCombat.set(this, true);
    }

    public void update() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof stsjorbsmod.cards.cull.ReapAndSow) {
                c.misc = 0;
            }
        }
        this.isDone = true;
    }
}
