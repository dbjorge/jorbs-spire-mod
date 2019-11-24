package stsjorbsmod.console;

import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.actions.SnapAction;

public class MemorySnapCommand extends ConsoleCommand {
    public MemorySnapCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        AbstractDungeon.actionManager.addToBottom(new SnapAction(AbstractDungeon.player));
    }
}
