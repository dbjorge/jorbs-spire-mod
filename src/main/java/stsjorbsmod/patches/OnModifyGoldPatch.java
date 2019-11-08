package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.IOnModifyGoldListener;

public class OnModifyGoldPatch {
    private static void notifyModifyGold(AbstractPlayer player) {
        for (AbstractPower p : player.powers) {
            if (p instanceof IOnModifyGoldListener) {
                ((IOnModifyGoldListener) p).onModifyGold(player);
            }
        }
        MemoryManager memoryManager = MemoryManager.forPlayer(player);
        if (memoryManager != null) {
            for (AbstractMemory m : memoryManager.currentMemories()) {
                m.onModifyGold(player);
            }
        }
        AbstractDungeon.onModifyPower();
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "gainGold"
    )
    public static class AbstractPlayer_gainGold
    {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this)
        {
            notifyModifyGold(__this);
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "loseGold"
    )
    public static class AbstractPlayer_loseGold
    {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this)
        {
            notifyModifyGold(__this);
        }
    }
}