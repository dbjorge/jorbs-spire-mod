package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.OnModifyGoldSubscriber;

public class OnModifyGoldPatch {
    private static void notifyModifyGold(AbstractPlayer player) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            for (AbstractPower p : player.powers) {
                if (p instanceof OnModifyGoldSubscriber) {
                    ((OnModifyGoldSubscriber) p).onModifyGold(player);
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