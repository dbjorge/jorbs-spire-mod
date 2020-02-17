package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.effects.ScalingLaserEffect;

public class RayOfFrostAction extends AbstractGameAction {
    private DamageInfo damageInfo;

    public RayOfFrostAction(AbstractCreature target, DamageInfo damageInfo) {
        this.source = damageInfo.owner;
        this.target = target;
        this.damageInfo = damageInfo;
    }


    @Override
    public void update() {
        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1", 0.7F);
        AbstractDungeon.effectList.add(new BorderFlashEffect(Color.SKY.cpy()));
        AbstractDungeon.effectList.add(new ScalingLaserEffect(source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY, Color.SKY.cpy(), Color.CYAN.cpy(), damageInfo.output));
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_LIGHT, true));
        target.damage(damageInfo);
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
        }
        if (!Settings.FAST_MODE) {
            addToTop(new WaitAction(0.1F));
        }
        isDone = true;
    }
}
