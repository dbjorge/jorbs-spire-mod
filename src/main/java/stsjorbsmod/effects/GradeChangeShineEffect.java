package stsjorbsmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;

import java.util.function.Supplier;

public class GradeChangeShineEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private boolean clang1 = false;
    private boolean clang2 = false;
    Supplier<Long> playSound;
    private float baseDuration;

    public GradeChangeShineEffect(float x, float y, float duration, Supplier<Long> playSound) {
        this.x = x;
        this.y = y;
        baseDuration = this.duration = duration;
        this.playSound = playSound;
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
        UpgradeHammerImprintEffect hammer = new UpgradeHammerImprintEffect(x, y);
        hammer.duration = this.duration;
        AbstractDungeon.topLevelEffectsQueue.add(hammer);
        if (!Settings.DISABLE_EFFECTS) {
            for (int i = 0; i < 30; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineParticleEffect(x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, y + MathUtils.random(-10.0F, 10.0F) * Settings.scale));
            }

        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
