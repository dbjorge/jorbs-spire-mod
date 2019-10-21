package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import stsjorbsmod.cards.IOnDrawCardSubscriber;

public class OnDrawCardSubscriberPatch {
    @SpirePatch(
            clz = CardGroup.class,
            method = "addToHand"
    )
    public static class CardGroup_addToHand
    {
        @SpireInsertPatch(
                rloc=0,
                localvars = {"c"}
        )
        public static void patch(CardGroup __this, AbstractCard c)
        {
            if (c instanceof IOnDrawCardSubscriber) {
                ((IOnDrawCardSubscriber)c).onDraw();
            }
        }
    }
}