package stsjorbsmod.console;

import basemod.devcommands.ConsoleCommand;

public class MemoryCommand extends ConsoleCommand {
    // Call from receivePostInitialize
    public static void register() {
        ConsoleCommand.addCommand("memory", MemoryCommand.class);
    }

    public MemoryCommand() {
        maxExtraTokens = 2;
        minExtraTokens = 1;
        requiresPlayer = true;
        simpleCheck = true;

        followup.put("clarify", MemoryClarifyCommand.class);
        followup.put("forget", MemoryForgetCommand.class);
        followup.put("remember", MemoryRememberCommand.class);
        followup.put("snap", MemorySnapCommand.class);
    }

    @Override
    public void execute(String[] tokens, int depth) {
        errorMsg();
    }
}
