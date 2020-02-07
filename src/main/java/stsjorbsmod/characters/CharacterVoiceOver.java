package stsjorbsmod.characters;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterVoiceOver {
    public static HashMap<PlayerClass, HashMap<String, ArrayList<String>>> characterKeyFiles = new HashMap<>();

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

    public static boolean play(String key) {
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
        ArrayList<String> candidateFiles = keyFiles.get(key);
        int index = MathUtils.random(candidateFiles.size() - 1);
        String fileName = candidateFiles.get(index);

        CardCrawlGame.sound.playV(JorbsMod.makeID(fileName), 1.2F);
        return true;
    }

    public static boolean playForBattle() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (play(m.id)) {
                return true;
            }
        }
        return false;
    }
}
