package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BlockCommand extends ConsoleCommand {
    // Call from receivePostInitialize
    public static void register() {
        ConsoleCommand.addCommand("block", BlockCommand.class);
    }

    public BlockCommand() {
        maxExtraTokens = 1;
        minExtraTokens = 1;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String requiredBlockAmount = tokens.length > depth ? tokens[depth] : null;
        try {
            int amount = Integer.parseInt(requiredBlockAmount);
            ConsoleTargeting.applyToTargetCreature(c -> AbstractDungeon.actionManager.addToBottom(new GainBlockAction(c, amount)));
        } catch (NumberFormatException e) {
            this.errorMsg();
        }
    }

    @Override
    protected void errorMsg() {
        DevConsole.log("Usage: block [amount]");
    }
}
