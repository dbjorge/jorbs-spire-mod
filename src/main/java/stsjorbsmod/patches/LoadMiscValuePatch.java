package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import stsjorbsmod.cards.CustomJorbsModCard;

import java.util.Arrays;
import java.util.Collections;

public class LoadMiscValuePatch {
    @SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
    public static class CardLibrary_getCopyPatch {
        @SpireInsertPatch(locator = CardID_StringEqualsLocator.class, localvars = "retVal")
        public static void patch(String key, int upgradeTime, int misc, AbstractCard retVal) {
            if(retVal instanceof CustomJorbsModCard) {
                ((CustomJorbsModCard) retVal).applyLoadedMiscValue(misc);
            }
            SpireReturn.Continue();
        }
    }

    private static class  CardID_StringEqualsLocator extends SpireInsertLocator{
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher cardCardIDMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "cardID");
            Matcher stringEqualsMatcher = new Matcher.MethodCallMatcher(String.class, "equals");
            return LineFinder.findInOrder(ctMethodToPatch, Arrays.asList(cardCardIDMatcher), stringEqualsMatcher);
        }
    }
}
