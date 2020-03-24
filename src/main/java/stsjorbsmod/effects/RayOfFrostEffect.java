package stsjorbsmod.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class RayOfFrostEffect extends AbstractGameEffect {
    private AbstractCreature source;
    private AbstractCreature target;
    private int amount;

    public RayOfFrostEffect(AbstractCreature source, AbstractCreature target, int amount) {
        this.source = source;
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1", 0.7F);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY.cpy()));
        AbstractDungeon.effectsQueue.add(new ScalingLaserEffect(
                source.hb.cX,
                source.hb.cY,
                target.hb.cX,
                target.hb.cY,
                Color.SKY.cpy(),
                Color.CYAN.cpy(),
                amount));
        isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) { }

    @Override
    public void dispose() { }
}
