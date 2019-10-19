package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.IGoldListenerPower;

public class GoldListenerPowerPatch {
    private static void notifyGoldListenerPowers(AbstractPlayer player) {
        for (AbstractPower p : player.powers) {
            if (p instanceof IGoldListenerPower) {
                ((IGoldListenerPower) p).onGoldModified(player);
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
            notifyGoldListenerPowers(__this);
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
            notifyGoldListenerPowers(__this);
        }
    }
}