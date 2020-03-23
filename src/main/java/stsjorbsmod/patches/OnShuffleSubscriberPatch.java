package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.cards.OnShuffleSubscriber;

public class OnShuffleSubscriberPatch {
    @SpirePatch(
            clz = EmptyDeckShuffleAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = { }
    )
    public static class AbstractPlayer_draw
    {
        @SpirePostfixPatch
        public static void Postfix(EmptyDeckShuffleAction __this)
        {
            // checks for OnShuffleSubscriber of each card in Exhaust pile
            for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                if (c instanceof OnShuffleSubscriber) {
                    ((OnShuffleSubscriber) c).onShuffle();
                }
            }
        }
    }
}