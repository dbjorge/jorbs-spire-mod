package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.actions.GainMemoryClarityAction;
import stsjorbsmod.actions.RememberRandomNewMemoryAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.powers.memories.AbstractMemoryPower;
import stsjorbsmod.util.MemoryPowerUtils;

import java.util.ArrayList;

public class MemoryClarifyCommand extends ConsoleCommand {
    public MemoryClarifyCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        if (tokens.length > depth) {
            errorMsg();
            return;
        }

        AbstractDungeon.actionManager.addToBottom(new GainMemoryClarityAction(AbstractDungeon.player, AbstractDungeon.player));
    }
}
