package stsjorbsmod.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

// Tracks a delay -> dampen master -> play VO -> undampen state machine
public class Voiceover {
    private static final float DAMPENING_RESTORE_DURATION = 0.5F;
    private static final float DAMPENING_FACTOR = 0.4F;
    private static final float PLAY_DURATION = 4.0F;

    private final Sfx sfx;
    private final float dampeningDuration;

    enum State {
        INITIAL_DELAY,
        DAMPENING_MASTER_VOLUME,
        PLAYING_SFX,
        RESTORING_MASTER_VOLUME,
        DONE
    }
    private State state;
    private float duration;

    public Voiceover(Sfx sfx, float startingDelay, float dampeningDuration) {
        this.sfx = sfx;
        this.dampeningDuration = dampeningDuration;

        if (startingDelay > 0.0F) {
            this.duration = startingDelay;
            this.state = State.INITIAL_DELAY;
        } else {
            this.duration = dampeningDuration;
            this.state = State.DAMPENING_MASTER_VOLUME;
        }
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();

        switch (state) {
            case INITIAL_DELAY:
                if (this.duration <= 0.0F) {
                    this.state = State.DAMPENING_MASTER_VOLUME;
                    this.duration = Math.max(dampeningDuration, 0.01F);
                }
                break;

            case DAMPENING_MASTER_VOLUME:
                float dampenProgress = dampeningDuration <= 0.0F ? 1.0F : MathUtils.clamp(1 - duration / dampeningDuration, 0F, 1F);
                VoiceoverMaster.MASTER_DAMPENING_FACTOR = MathUtils.lerp(1.0F, DAMPENING_FACTOR, dampenProgress);
                CardCrawlGame.music.updateVolume();

                if (this.duration <= 0.0F) {
                    sfx.play(Settings.MASTER_VOLUME * VoiceoverMaster.VOICEOVER_VOLUME * VoiceoverMaster.VOICEOVER_VOLUME_MODIFIER);

                    this.state = State.PLAYING_SFX;
                    this.duration = PLAY_DURATION;
                }
                break;

            case PLAYING_SFX:
                if (this.duration <= 0.0F) {
                    this.state = State.RESTORING_MASTER_VOLUME;
                    this.duration = DAMPENING_RESTORE_DURATION;
                }
                break;

            case RESTORING_MASTER_VOLUME:
                float restoreProgress = MathUtils.clamp(1 - duration / DAMPENING_RESTORE_DURATION, 0F, 1F);
                VoiceoverMaster.MASTER_DAMPENING_FACTOR = MathUtils.lerp(DAMPENING_FACTOR, 1.0F, restoreProgress);
                CardCrawlGame.music.updateVolume();

                if (this.duration <= 0.0F) {
                    VoiceoverMaster.MASTER_DAMPENING_FACTOR = 1.0F;
                    this.state = State.DONE;
                }
                break;
        }
    }

    public boolean isDone() {
        return state == State.DONE;
    }
}
