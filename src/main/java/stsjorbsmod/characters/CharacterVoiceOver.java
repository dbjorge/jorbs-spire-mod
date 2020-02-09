package stsjorbsmod.characters;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.SphericGuardian;
import com.megacrit.cardcrawl.random.Random;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterVoiceOver {
    public static final float VOICEOVER_VOLUME_MODIFIER = 2.4F; // compensating for volume of files
    public static float VOICEOVER_VOLUME = 0.5F; // set by mod settings

    public static HashMap<PlayerClass, HashMap<String, ArrayList<String>>> characterKeyFiles = new HashMap<>();

    public static boolean isMuted() {
        return VOICEOVER_VOLUME <= 0.0F;
    }

    // You can register multiple files for the same key; play will choose randomly between them
    // key should be of form "AwakenedOne"
    // fileName should be of form "wanderer/AwakenedOne_2"
    public static void register(PlayerClass playerClass, String key, String fileName) {
        BaseMod.addAudio(JorbsMod.makeID(fileName), JorbsMod.makeVoiceOverPath(fileName));

        if (!characterKeyFiles.containsKey(playerClass)) {
            characterKeyFiles.put(playerClass, new HashMap<>());
        }
        HashMap<String, ArrayList<String>> keyFiles = characterKeyFiles.get(playerClass);
        if (!keyFiles.containsKey(key)) {
            keyFiles.put(key, new ArrayList<>());
        }
        keyFiles.get(key).add(fileName);
    }

    public static boolean hasVoiceoverFor(String key) {
        if (AbstractDungeon.player == null) {
            return false;
        }
        if (!characterKeyFiles.containsKey(AbstractDungeon.player.chosenClass)) {
            return false;
        }
        HashMap<String, ArrayList<String>> keyFiles = characterKeyFiles.get(AbstractDungeon.player.chosenClass);
        if (!keyFiles.containsKey(key)) {
            return false;
        }

        return !keyFiles.get(key).isEmpty();
    }

    public static boolean play(String key) {
        if (!hasVoiceoverFor(key)) {
            return false;
        }

        ArrayList<String> candidateFiles = characterKeyFiles.get(AbstractDungeon.player.chosenClass).get(key);
        int index = MathUtils.random(candidateFiles.size() - 1);
        String fileName = candidateFiles.get(index);

        CardCrawlGame.sound.playV(JorbsMod.makeID(fileName), VOICEOVER_VOLUME * VOICEOVER_VOLUME_MODIFIER);
        return true;
    }

    public static String keyForCurrBattle() {
        // The Sentry + SphericGuardian fight uses an elite enemy in a non-elite fight; we need to special
        // case it to avoid playing the 3-sentries line in that fight
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.id.equals(SphericGuardian.ID)) {
                return null;
            }
        }

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (hasVoiceoverFor(m.id)) {
                return m.id;
            }
        }

        return null;
    }
}
