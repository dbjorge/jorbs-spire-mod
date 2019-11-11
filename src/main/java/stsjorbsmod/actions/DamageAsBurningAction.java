package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.effects.SmallLaserTestEffect;
import stsjorbsmod.powers.BurningPower;

public class DamageAsBurningAction extends AbstractGameAction {
    private DamageInfo damageInfo;

    public DamageAsBurningAction(AbstractCreature target, DamageInfo damageInfo) {
        setValues(target, damageInfo);
        this.damageInfo = damageInfo;
    }

    @Override
    public void update() {
        target.damage(damageInfo);
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(target, source, new BurningPower(target, source, amount)));
        isDone = true;
    }
}
