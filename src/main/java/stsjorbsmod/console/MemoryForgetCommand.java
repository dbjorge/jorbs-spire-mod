package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryUtils;

import java.util.ArrayList;

public class MemoryForgetCommand extends ConsoleCommand {
    public MemoryForgetCommand() {
        maxExtraTokens = 1;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String optionalId = tokens.length > depth ? tokens[depth] : null;

        AbstractMemory currentMemory = MemoryUtils.getCurrentMemory(AbstractDungeon.player);
        if (optionalId == null && currentMemory == null) {
            DevConsole.log("Ignoring command: no active memory to remove");
        } else {
            String idToRemove = (optionalId != null) ? optionalId : currentMemory.ID;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, idToRemove));
        }
    }

    @Override
    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        return MemoryUtils.allActiveMemoryIDs(AbstractDungeon.player);
    }

    @Override
    public void errorMsg() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* memory add");
        DevConsole.log("* memory add [memoryPowerId]");
    }
}
