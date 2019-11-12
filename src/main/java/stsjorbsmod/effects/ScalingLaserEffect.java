package stsjorbsmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ScalingLaserEffect extends AbstractGameEffect {
    private float sX;
    private float sY;
    private float dX;
    private float dY;
    private float dst;
    private static final float DUR = 0.5F;
    private static TextureAtlas.AtlasRegion img;
    private float beamThickness;
    private Color color2;

    public ScalingLaserEffect(float sX, float sY, float dX, float dY, Color color1, Color color2, int amount) {
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }

        this.sX = sX;
        this.sY = sY;
        this.dX = dX;
        this.dY = dY;
        this.dst = Vector2.dst(this.sX, this.sY, this.dX, this.dY) / Settings.scale;
        this.color = color1;
        this.color2 = color2;
        this.duration = DUR;
        this.startingDuration = DUR;
        this.rotation = MathUtils.atan2(dX - sX, dY - sY);
        this.rotation *= 57.295776F;
        this.rotation = -this.rotation + 90.0F;
        if (amount <= 0) {
            this.beamThickness = 0;
        } else {
            this.beamThickness = amount * 3f / 2f;
        }
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2.0F) {
            this.color.a = Interpolation.pow2In.apply(1.0F, 0.0F, (this.duration - 0.25F) * 4.0F);
        } else {
            this.color.a = Interpolation.bounceIn.apply(0.0F, 1.0F, this.duration * 4.0F);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(img,
                this.sX,
                (this.sY - (float) img.packedHeight / 2.0F + 10.0F) - (Settings.scale * (beamThickness / 2)),
                0.0F,
                (float) img.packedHeight / 2.0F,
                this.dst,
                50.0F + (Settings.scale * beamThickness),
                this.scale + MathUtils.random(-0.01F, 0.01F),
                this.scale,
                this.rotation);
        sb.setColor(this.color2);
        sb.draw(img,
                this.sX,
                this.sY - (float) img.packedHeight / 2.0F - (Settings.scale * beamThickness / 4),
                0.0F,
                (float) img.packedHeight / 2.0F,
                this.dst,
                MathUtils.random(50.0F, 90.0F) + (Settings.scale * beamThickness / 2),
                this.scale + MathUtils.random(-0.02F, 0.02F),
                this.scale,
                this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }

}
