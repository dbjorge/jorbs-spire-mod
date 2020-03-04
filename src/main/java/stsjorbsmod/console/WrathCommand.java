package stsjorbsmod.console;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.WrathMemory;

import java.util.ArrayList;

public class WrathCommand extends ConsoleCommand {
    // Call from receivePostInitialize
    public static void register() {
        ConsoleCommand.addCommand("wrath", WrathCommand.class);
    }

    public WrathCommand() {
        maxExtraTokens = 2;
        minExtraTokens = 2;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    public void execute(String[] tokens, int depth) {
        String rawCardIndex = tokens.length > depth ? tokens[depth] : null;
        String rawAmount = tokens.length > depth + 1 ? tokens[depth + 1] : null;

        try {
            int cardIndex = Integer.parseInt(rawCardIndex);
            int amount = Integer.parseInt(rawAmount);

            if (cardIndex < 1 || cardIndex > AbstractDungeon.player.hand.size() || amount < 1) {
                this.errorMsg();
                return;
            }

            AbstractCard c = AbstractDungeon.player.hand.group.get(cardIndex - 1);
            for (int i = 0; i < amount; ++i) {
                WrathMemory.permanentlyIncreaseCardDamage(c);
            }
        } catch (NumberFormatException e) {
            this.errorMsg();
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (depth == 1) {
            ArrayList<String> options = new ArrayList<>();
            for (int i = 1; i <= AbstractDungeon.player.hand.size(); ++i) {
                if (WrathMemory.isUpgradeCandidate(AbstractDungeon.player.hand.group.get(i-1))) {
                    options.add(Integer.toString(i));
                }
            }
            return options;
        }
        return null;
    }

    @Override
    protected void errorMsg() {
        DevConsole.log("Usage: wrath [cardIndex] [amount]");
    }
}
