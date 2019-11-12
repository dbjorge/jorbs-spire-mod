package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.patches.DamageAsBurningPatch;
import stsjorbsmod.powers.BurningPower;

public class DamageAsBurningAction extends AbstractGameAction {
    private DamageInfo damageInfo;

    public DamageAsBurningAction(AbstractCreature target, DamageInfo damageInfo) {
        setValues(target, damageInfo);
        this.damageInfo = damageInfo;
        DamageAsBurningPatch.isBurningField.isBurning.set(damageInfo, true);
    }

    @Override
    public void update() {
        target.damage(damageInfo);
        isDone = true;
    }
}
