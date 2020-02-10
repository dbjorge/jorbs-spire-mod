package stsjorbsmod.audio;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.random.Random;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceoverMaster {
    public static final float VOICEOVER_VOLUME_MODIFIER = 1.2F; // compensating for volume of files
    public static float VOICEOVER_VOLUME = 0.5F; // set by mod settings

    // non-voiceover volumes are multiplied by this; it is decreased while voiceovers are active
    public static float MASTER_DAMPENING_FACTOR = 1.0F;

    public static Map<PlayerClass, Map<String, List<Sfx>>> sfxByCharacterAndKey = new HashMap<>();

    private static Voiceover activeVoiceover;

    public static boolean isMuted() {
        return VOICEOVER_VOLUME <= 0.0F;
    }

    // You can register multiple files for the same key; play will choose randomly between them
    // key should be of form "AwakenedOne"
    // fileName should be of form "wanderer/AwakenedOne_2"
    public static void register(PlayerClass playerClass, String key, String fileName) {
        Sfx sfx = load(JorbsMod.makeVoiceOverPath(fileName));

        if (!sfxByCharacterAndKey.containsKey(playerClass)) {
            sfxByCharacterAndKey.put(playerClass, new HashMap<>());
        }
        Map<String, List<Sfx>> sfxByKey = sfxByCharacterAndKey.get(playerClass);

        if (!sfxByKey.containsKey(key)) {
            sfxByKey.put(key, new ArrayList<>());
        }
        sfxByKey.get(key).add(sfx);
    }

    public static void update() {
        if (activeVoiceover != null) {
            activeVoiceover.update();
            if (activeVoiceover.isDone()) {
                activeVoiceover = null;
            }
        }
    }

    public static void play(String key) {
        playSfx(getSfxByKey(key), 0.0F, 0.5F);
    }

    public static void playAfterDelay(String key, float delay) {
        playSfx(getSfxByKey(key), delay - 0.5F, 0.5F);
    }

    public static void playForCurrentBattle() {
        Sfx sfx = getSfxByCurrentBattle();

        // The heart fight's music starts with a music change involving a big loud trumpet note,
        // it's less jarring for that particular music to be dampened immediately
        boolean isHeartFight = AbstractDungeon.getMonsters().getMonster(CorruptHeart.ID) != null;
        float dampeningDuration = isHeartFight ? 0.0F : 0.5F;

        playSfx(sfx, 0.0F, dampeningDuration);
    }

    private static void playSfx(Sfx sfx, float startingDelay, float dampeningDuration) {
        if (sfx != null && !isMuted() && activeVoiceover == null) {
            activeVoiceover = new Voiceover(sfx, startingDelay, dampeningDuration);
        }
    }

    private static Sfx load(String file) {
        FileHandle sfxFile = Gdx.files.internal(file);
        if (!sfxFile.exists()) {
            throw new RuntimeException("JorbsMod: Voiceover file does not exist: " + file);
        }
        return new Sfx(file, false);
    }

    private static Sfx getSfxByKey(String key) {
        if (AbstractDungeon.player == null) {
            return null;
        }
        if (!sfxByCharacterAndKey.containsKey(AbstractDungeon.player.chosenClass)) {
            return null;
        }
        Map<String, List<Sfx>> sfxByKey = sfxByCharacterAndKey.get(AbstractDungeon.player.chosenClass);
        if (!sfxByKey.containsKey(key)) {
            return null;
        }
        List<Sfx> candidates = sfxByKey.get(key);
        if (candidates.isEmpty()) {
            return null;
        }
        int index = MathUtils.random(candidates.size() - 1);
        return candidates.get(index);
    }

    private static Sfx getSfxByCurrentBattle() {
        // The Sentry + SphericGuardian fight uses an elite enemy in a non-elite fight; we need to special
        // case it to avoid playing the 3-sentries line in that fight
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.id.equals(SphericGuardian.ID)) {
                return null;
            }
        }

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            Sfx sfxForCurrentMonster = getSfxByKey(m.id);
            if (sfxForCurrentMonster != null) {
                return sfxForCurrentMonster;
            }
        }

        return null;
    }
}
