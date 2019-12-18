package stsjorbsmod.console;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import stsjorbsmod.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PlaySoundCommand extends ConsoleCommand {
    // Call from receivePostInitialize
    public static void register() {
        ConsoleCommand.addCommand("playsound", PlaySoundCommand.class);
    }

    public PlaySoundCommand() {
        maxExtraTokens = 2;
        minExtraTokens = 1;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String requiredSoundId = tokens.length > depth ? tokens[depth] : null;

        if (tokens.length > depth + 1) {
            try {
                float pitchAdjustment = Float.parseFloat(tokens[depth + 1]);
                CardCrawlGame.sound.playA(requiredSoundId, pitchAdjustment);
            } catch(Exception e) {
                return;
            }
        } else {
            CardCrawlGame.sound.play(requiredSoundId);
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        HashMap<String, Sfx> map = ReflectionUtils.getPrivateField(CardCrawlGame.sound, SoundMaster.class, "map");
        return map.keySet().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
    }
}
