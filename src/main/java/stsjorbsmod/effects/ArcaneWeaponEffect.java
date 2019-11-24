package stsjorbsmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ArcaneWeaponEffect extends AbstractGameEffect {
    TextureAtlas.AtlasRegion img;
    private float targetX;
    private float targetY;

    public ArcaneWeaponEffect(float targetX, float targetY) {
        super();
        this.duration = this.startingDuration = 0.5F;
        img = ImageMaster.vfxAtlas.findRegion("combat/battleStartSword");
        this.targetX = targetX;
        this.targetY = targetY;
        this.color = new Color(0.75F, 0.4F, 0.3F, 1.0F);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        rotation = ((duration - startingDuration) * 180.0F) / startingDuration + 22.5F;
        sb.draw(img,
                targetX - MathUtils.sinDeg(rotation) * img.packedHeight / 2.0F,
                targetY + MathUtils.cosDeg(rotation) * img.packedHeight / 2.0F,
                0.0F,
                (float) img.packedHeight / 2.0F,
                img.packedWidth,
                img.packedHeight,
                scale,
                scale,
                rotation);
        sb.setColor(1.0F, 0.3F, 0.25F, 0.7F);
        sb.draw(img,
                targetX - MathUtils.sinDeg(rotation) * img.packedHeight / 1.25F / 2.0F,
                targetY + MathUtils.cosDeg(rotation) * img.packedHeight / 1.25F / 2.0F,
                0.0F,
                (float) img.packedHeight / 2.0F,
                img.packedWidth / 0.75F,
                img.packedHeight / 0.75F,
                scale,
                scale,
                rotation);
    }

    @Override
    public void dispose() {

    }
}
