package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.actions.GainMemoryClarityAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.util.MemoryPowerUtils;

import java.util.ArrayList;

public class MemoryClarifyCommand extends ConsoleCommand {
    public MemoryClarifyCommand() {
        maxExtraTokens = 1;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String optionalId = tokens.length > depth ? tokens[depth] : null;

        if (optionalId == null) {
            DevConsole.log("Clarifying currently-remembered memory (like Eye of the Storm)");
            AbstractDungeon.actionManager.addToBottom(new GainMemoryClarityAction(AbstractDungeon.player));
        } else {
            AbstractMemory newMemory = MemoryPowerUtils.newMemoryByID(tokens[2], AbstractDungeon.player, true);
            AbstractDungeon.actionManager.addToBottom(new RememberSpecificMemoryAction(newMemory));
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        return MemoryPowerUtils.allPossibleMemoryIDs();
    }
}
