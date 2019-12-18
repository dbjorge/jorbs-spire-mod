package stsjorbsmod.console;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.MemoryManager;

public class MemoryForgetCommand extends ConsoleCommand {
    public MemoryForgetCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        MemoryManager.forPlayer(AbstractDungeon.player).forgetCurrentMemory();
    }
}
