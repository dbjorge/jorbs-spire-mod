package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class FountainOfCleansingPatch {
    public static final String FountainOfCleansingPatchName = FountainOfCleansingPatch.class.getName();

    @SpirePatch(clz = AbstractDungeon.class, method = "getShrine")
    public static class AbstractDungeon_getShrine {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall mc) throws CannotCompileException {
                    if (mc.getMethodName().equals("isCursed")) {
                        mc.replace(String.format("{ $_ = $proceed($$) && %1$s.hasRemovableCurses(); }", FountainOfCleansingPatchName));
                    }
                }
            };
        }
    }

    public static boolean hasRemovableCurses() {
        int numRemoveableCurses = 0;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.type == AbstractCard.CardType.CURSE && c.cardID != "Necronomicurse" && c.cardID != "CurseOfTheBell" && c.cardID != "AscendersBane" && !BottledBurdenPatch.isInBottleBurden(c)){
                numRemoveableCurses++;
            }
        }
        return numRemoveableCurses > 0;
    }
}
