package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;
import stsjorbsmod.cards.OnDrawCardSubscriber;

public class OnDrawCardSubscriberPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "draw",
            paramtypez = { int.class }
    )
    public static class AbstractPlayer_draw
    {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"c"}
        )
        public static void patch(AbstractPlayer __this, AbstractCard c)
        {
            if (c instanceof OnDrawCardSubscriber) {
                ((OnDrawCardSubscriber)c).onDraw();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}