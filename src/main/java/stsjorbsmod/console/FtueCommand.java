package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import stsjorbsmod.tips.JorbsModTipTracker;

import java.util.ArrayList;

public class FtueCommand extends ConsoleCommand {
    // Call from receivePostInitialize
    public static void register() {
        ConsoleCommand.addCommand("stsjorbsmod:ftue", FtueCommand.class);
    }

    public FtueCommand() {
        maxExtraTokens = 1;
        minExtraTokens = 1;
        requiresPlayer = false;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String subcommand = tokens.length > depth ? tokens[depth] : null;
        if ("reset".equals(subcommand)) {
            JorbsModTipTracker.reset();
        } else {
            this.errorMsg();
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (depth == 1) {
            ArrayList<String> options = new ArrayList<>();
            options.add("reset");
            return options;
        }
        return null;
    }

    @Override
    protected void errorMsg() {
        DevConsole.log("Usage: stsjorbsmod:ftue reset");
    }
}
