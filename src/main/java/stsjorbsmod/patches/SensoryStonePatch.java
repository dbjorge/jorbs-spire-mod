package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import javassist.CtBehavior;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;
import java.util.Collections;

@SpirePatch(
        clz = SensoryStone.class,
        method = "getRandomMemory"
)
public class SensoryStonePatch {
    public static final String[] DESCRIPTIONS = CardCrawlGame.languagePack.getEventString(JorbsMod.makeID("SensoryStone")).DESCRIPTIONS;

    @SpireInsertPatch(locator = Locator.class, localvars = "memories")
    public static void patch(SensoryStone __this, ArrayList<String> memories) {
        memories.clear();
        memories.add(DESCRIPTIONS[0]);
        memories.add(DESCRIPTIONS[1]);
        memories.add(DESCRIPTIONS[2]);
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Collections.class, "shuffle");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
