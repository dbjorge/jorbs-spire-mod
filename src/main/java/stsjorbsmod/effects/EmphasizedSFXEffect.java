package stsjorbsmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import stsjorbsmod.characters.CharacterVoiceOver;

// Dampens master volume while effect is going
public class EmphasizedSFXEffect extends AbstractGameEffect {
    private static final float DAMPENING_DURATION = 0.5F;
    private static final float DAMPENING_FACTOR = 0.4F;
    private static final float PLAY_DURATION = 4.0F;

    private final Runnable playSoundCallback;

    enum State {
        INITIAL_DELAY,
        DAMPENING_MASTER_VOLUME,
        PLAYING_SFX,
        RESTORING_MASTER_VOLUME
    }
    private State state;

    private float originalMasterVolume;
    private float dampenedMasterVolume;

    public EmphasizedSFXEffect(Runnable playSound) {
        this(playSound, -1.0F);
    }

    public EmphasizedSFXEffect(Runnable playSoundCallback, float startingDelay) {
        this.playSoundCallback = playSoundCallback;
        this.state = State.INITIAL_DELAY;
        this.duration = startingDelay;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        switch(state) {
            case INITIAL_DELAY:
                if (this.duration < 0.0F) {
                    this.state = State.DAMPENING_MASTER_VOLUME;
                    this.duration = DAMPENING_DURATION;
                    this.originalMasterVolume = Settings.MASTER_VOLUME;
                    this.dampenedMasterVolume = this.originalMasterVolume * DAMPENING_FACTOR;
                }
                break;

            case DAMPENING_MASTER_VOLUME:
                float dampenProgress = MathUtils.clamp(1 - duration / DAMPENING_DURATION, 0F, 1F);
                Settings.MASTER_VOLUME = MathUtils.lerp(originalMasterVolume, dampenedMasterVolume, dampenProgress);
                CardCrawlGame.music.updateVolume();

                if (this.duration < 0.0F) {
                    Settings.MASTER_VOLUME = originalMasterVolume;
                    playSoundCallback.run();
                    Settings.MASTER_VOLUME = dampenedMasterVolume;

                    this.state = State.PLAYING_SFX;
                    this.duration = PLAY_DURATION;
                }
                break;

            case PLAYING_SFX:
                if (this.duration < 0.0F) {
                    this.state = State.RESTORING_MASTER_VOLUME;
                    this.duration = DAMPENING_DURATION;
                }
                break;

            case RESTORING_MASTER_VOLUME:
                float restoreProgress = MathUtils.clamp(1 - duration / DAMPENING_DURATION, 0F, 1F);
                Settings.MASTER_VOLUME = MathUtils.lerp(dampenedMasterVolume, originalMasterVolume, restoreProgress);
                CardCrawlGame.music.updateVolume();

                if (this.duration < 0.0F) {
                    Settings.MASTER_VOLUME = originalMasterVolume;
                    this.isDone = true;
                }
                break;
        }
    }

    public void render(SpriteBatch sb) { }
    public void dispose() { }
}
