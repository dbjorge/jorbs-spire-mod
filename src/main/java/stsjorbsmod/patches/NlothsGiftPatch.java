package stsjorbsmod.patches;

import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.relics.FragileMindRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NlothsGiftPatch {

    public static class ClonePlayerRelicsWithoutFragileMind extends ExprEditor {
        @Override
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName())
                    && fieldAccess.getFieldName().equals("relics")) {
                fieldAccess.replace("{ $_ = (" + NlothsGiftPatch.class.getName() + ".clonePlayerRelicsWithoutFragileMind($proceed())); }");
            }
        }
    }

    /**
     * Removes FragileMindRelic if found in the iterator
     *
     * @param iter
     */
    public static void removeFragileMindRelic(Iterator<AbstractRelic> iter) {
        while (iter.hasNext()) {
            if (FragileMindRelic.ID.equals(iter.next().relicId)) {
                iter.remove();
            }
        }
    }

    /**
     * This is called only by edited expressions in the main game. See the following SpirePatch.
     *
     * @param playerRelics
     * @return
     */
    public static ArrayList<AbstractRelic> clonePlayerRelicsWithoutFragileMind(ArrayList<AbstractRelic> playerRelics) {
        ArrayList<AbstractRelic> relics = new ArrayList<>(playerRelics);
        removeFragileMindRelic(relics.iterator());
        return relics;
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getShrine")
    public static class AbstractDungeon_getShrine_NlothCheck {
        public static ExprEditor Instrument() {
            return new ClonePlayerRelicsWithoutFragileMind();
        }
    }

    @SpirePatch(clz = Nloth.class, method = SpirePatch.CONSTRUCTOR)
    public static class Nloth_ctor_RemoveFragileMind {
        /**
         * Removes FragileMindRelic from the list of relics for N'loth to request.
         */
        public static ExprEditor Instrument() {
            return new ClonePlayerRelicsWithoutFragileMind();
        }
//        @SpireInsertPatch(locator = Nloth_ctor_RemoveFragileMind.Locator.class, localvars = "relics")
//        public static void patch(Nloth _this, Iterable<AbstractRelic> relics) {
//            removeFragileMindRelic(relics.iterator());
//        }
//
//        private static class Locator extends SpireInsertLocator {
//            public int[] Locate(CtBehavior ctBehavior) throws Exception {
//                Matcher collectionsShuffleMatcher = new Matcher.MethodCallMatcher(Collections.class, "shuffle");
//                return LineFinder.findInOrder(ctBehavior, collectionsShuffleMatcher);
//            }
//        }
    }
}
