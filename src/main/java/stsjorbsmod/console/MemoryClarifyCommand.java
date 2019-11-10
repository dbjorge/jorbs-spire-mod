package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryUtils;

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
            AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(AbstractDungeon.player));
        } else if (optionalId.equals("all")) {
            for (String id : MemoryUtils.allPossibleMemoryIDs()) {
                AbstractDungeon.actionManager.addToBottom(new GainSpecificClarityAction(AbstractDungeon.player, id));
            }
        } else {
            AbstractDungeon.actionManager.addToBottom(new GainSpecificClarityAction(AbstractDungeon.player, tokens[2]));
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> options = MemoryUtils.allPossibleMemoryIDs();
        options.add("all");
        return options;
    }
}
