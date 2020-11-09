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
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.VoiceoverMasterPatch;
import stsjorbsmod.util.CombatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.megacrit.cardcrawl.helpers.MonsterHelper.LAGAVULIN_ENC;
import static com.megacrit.cardcrawl.helpers.MonsterHelper.LAGAVULIN_EVENT_ENC;

public class VoiceoverMaster {
    private static final class VoiceoverInfo {
        public final Sfx sfx;
        public final String subtitle;
        public VoiceoverInfo(Sfx sfx, String subtitle) {
            this.sfx = sfx;
            this.subtitle = subtitle;
        }
    }

    public static final float VOICEOVER_VOLUME_MODIFIER = 1.35F; // compensating for volume of files

    // set by mod settings
    public static float VOICEOVER_VOLUME = 0.5F;
    public static boolean VOICEOVER_SUBTITLES_ENABLED = true;

    // non-voiceover volumes are multiplied by this; it is decreased while voiceovers are active
    public static float MASTER_DAMPENING_FACTOR = 1.0F;

    public static Map<PlayerClass, Map<String, List<VoiceoverInfo>>> sfxByCharacterAndKey = new HashMap<>();

    private static Voiceover activeVoiceover;

    public static boolean isMuted() {
        return VOICEOVER_VOLUME <= 0.0F;
    }

    // You can register multiple files for the same key; play will choose randomly between them
    // key should be of form "AwakenedOne"
    // fileName should be of form "wanderer/AwakenedOne_2"
    public static void register(PlayerClass playerClass, String key, String fileName, String subtitle) {
        Sfx sfx = load(JorbsMod.makeVoiceOverPath(fileName));

        if (!sfxByCharacterAndKey.containsKey(playerClass)) {
            sfxByCharacterAndKey.put(playerClass, new HashMap<>());
        }
        Map<String, List<VoiceoverInfo>> sfxByKey = sfxByCharacterAndKey.get(playerClass);

        if (!sfxByKey.containsKey(key)) {
            sfxByKey.put(key, new ArrayList<>());
        }
        sfxByKey.get(key).add(new VoiceoverInfo(sfx, subtitle));
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
        if (isInBattle()) {
            VoiceoverInfo sfx = getSfxByCurrentBattle();

            // The heart fight's music starts with a music change involving a big loud trumpet note,
            // it's less jarring for that particular music to be dampened immediately
            boolean isHeartFight = AbstractDungeon.getMonsters().getMonster(CorruptHeart.ID) != null;
            float dampeningDuration = isHeartFight ? 0.0F : 0.5F;

            playSfx(sfx, 0.0F, dampeningDuration);
        }
    }

    private static void playSfx(VoiceoverInfo sfx, float startingDelay, float dampeningDuration) {
        if (sfx != null && !isMuted() && activeVoiceover == null) {
            activeVoiceover = new Voiceover(sfx.sfx, sfx.subtitle, startingDelay, dampeningDuration);
        }
    }

    private static Sfx load(String file) {
        FileHandle sfxFile = Gdx.files.internal(file);
        if (!sfxFile.exists()) {
            throw new RuntimeException("JorbsMod: Voiceover file does not exist: " + file);
        }
        return new Sfx(file, false);
    }

    private static VoiceoverInfo getSfxByKey(String key) {
        if (AbstractDungeon.player == null) {
            return null;
        }
        if (!sfxByCharacterAndKey.containsKey(AbstractDungeon.player.chosenClass)) {
            return null;
        }
        Map<String, List<VoiceoverInfo>> sfxByKey = sfxByCharacterAndKey.get(AbstractDungeon.player.chosenClass);
        if (!sfxByKey.containsKey(key)) {
            return null;
        }
        List<VoiceoverInfo> candidates = sfxByKey.get(key);
        if (candidates.isEmpty()) {
            return null;
        }
        int index = MathUtils.random(candidates.size() - 1);
        return candidates.get(index);
    }

    private static boolean isInBattle() {
        return CombatUtils.isInCombat() && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null;
    }

    private static VoiceoverInfo getSfxByCurrentBattle() {
        String encounterKey = VoiceoverMasterPatch.MonsterGroup_class.encounterKeyField.get(AbstractDungeon.getCurrRoom().monsters);
        if (encounterKey == null) {
            return null;
        }

        if (encounterKey.equals(LAGAVULIN_EVENT_ENC)) {
            encounterKey = LAGAVULIN_ENC;
        }

        return getSfxByKey(encounterKey);
    }
}
