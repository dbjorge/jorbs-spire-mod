package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.Nloth;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.relics.FragileMindRelic;

import java.util.ArrayList;

public class NlothsGiftPatch {

    /**
     * This is called only by edited expressions in the main game. See the following SpirePatch.
     *
     * @param playerRelics ArrayList of AbstractRelic. This exactly matches the return type
     * @return ArrayList of AbstractRelics. This exactly matches the parameter type
     */
    public static ArrayList<AbstractRelic> clonePlayerRelicsWithoutFragileMind(ArrayList<AbstractRelic> playerRelics) {
        ArrayList<AbstractRelic> relics = new ArrayList<>(playerRelics.size());
        playerRelics.forEach(r -> {
            if (!FragileMindRelic.ID.equals(r.relicId)) {
                relics.add(r);
            }
        });
        return relics;
    }

    public static class ClonePlayerRelicsWithoutFragileMind extends ExprEditor {
        @Override
        public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName())
                    && fieldAccess.getFieldName().equals("relics")) {
                fieldAccess.replace("{ $_ = (" + NlothsGiftPatch.class.getName() + ".clonePlayerRelicsWithoutFragileMind($proceed())); }");
            }
        }
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
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getShrine", paramtypez = { Random.class })
    public static class AbstractDungeon_getShrine_NlothLogger {
        @SpireInsertPatch(locator = NlothsGiftPatch.AbstractDungeon_getShrine_NlothLogger.Locator.class, localvars = "tmp")
        public static void patch(Random rng, ArrayList<String> tmp) {
            JorbsMod.logger.info(tmp);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }
}
