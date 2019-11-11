package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class ScorchingRayAction extends AbstractGameAction {

    public ScorchingRayAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
//            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.GOLDENROD)));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(
                new SmallLaserEffect(source.hb.cX, source.hb.cX, target.hb.cX, target.hb.cY), 0.1F));
        AbstractDungeon.actionManager.addToBottom(new DamageAsBurningAction(target, new DamageInfo(source, amount)));
        isDone = true;
    }
}
