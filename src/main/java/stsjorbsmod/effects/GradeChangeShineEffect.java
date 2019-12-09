package stsjorbsmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import stsjorbsmod.util.ReflectionUtils;

import java.util.function.Supplier;

public class GradeChangeShineEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private boolean clang1 = false;
    private boolean clang2 = false;
    Supplier<Long> playSound;
    private float baseDuration;
    private Supplier<AbstractGameEffect> effectSupplier;
    private Color color;

    public GradeChangeShineEffect(float x, float y, float duration, Supplier<Long> playSound, Supplier<AbstractGameEffect> effectSupplier, Color color) {
        this.x = x;
        this.y = y;
        baseDuration = this.duration = duration;
        this.playSound = playSound;
        this.effectSupplier = effectSupplier;
        this.color = color;
    }

    public void update() {
        if (duration / baseDuration < 0.6F && !clang1) {
            playSound.get();
            clang1 = true;
            clank(x - 80.0F * Settings.scale, y + 0.0F * Settings.scale);
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

        if (duration / baseDuration < 0.2F && !clang2) {
            clang2 = true;
            clank(x + 90.0F * Settings.scale, y - 110.0F * Settings.scale);
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            clank(this.x + 30.0F * Settings.scale, y + 120.0F * Settings.scale);
            isDone = true;
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
        }

    }

    private void clank(float x, float y) {
        AbstractGameEffect hammer = new UpgradeHammerImprintEffect(x, y);
        hammer.duration = this.duration;
        if(color != null) {
            ReflectionUtils.setPrivateField(hammer, AbstractGameEffect.class, "color", color);
        }
        AbstractDungeon.topLevelEffectsQueue.add(hammer);
        if (!Settings.DISABLE_EFFECTS) {
            for (int i = 0; i < 30; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(effectSupplier.get());
            }
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
