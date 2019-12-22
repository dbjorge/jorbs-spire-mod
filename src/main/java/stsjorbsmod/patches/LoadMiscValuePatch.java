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
        @SpirePostfixPatch
        public static AbstractCard patch(AbstractCard __result, String key, int upgradeTime, int misc) {
            if(misc != 0 && __result instanceof CustomJorbsModCard) {
                ((CustomJorbsModCard) __result).applyLoadedMiscValue(misc);
            }
            return __result;
        }
    }
}
