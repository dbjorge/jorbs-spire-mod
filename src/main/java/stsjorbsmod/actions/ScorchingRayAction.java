package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import stsjorbsmod.effects.ScalingLaserEffect;
import stsjorbsmod.powers.BurningPower;

public class ScorchingRayAction extends AbstractGameAction {
    public ScorchingRayAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.GOLDENROD)));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(
                new ScalingLaserEffect(source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, Color.ORANGE.cpy(), Color.RED.cpy(), amount), 0.1F));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new BurningPower(target, source, amount)));

        isDone = true;
    }
}
