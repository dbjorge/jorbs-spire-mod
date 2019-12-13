package stsjorbsmod.patches;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import javassist.CtBehavior;
import stsjorbsmod.JorbsMod;

// Based on StSLib's ClickableRelicUpdatePatch
@SpirePatch(
        clz = OverlayMenu.class,
        method = "update"
)
public class ShovelClickPatch {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(JorbsMod.makeID("DigEmote")).TEXT;

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"r"}
    )
    public static void Insert(OverlayMenu __instance, AbstractRelic relic) {
        if (relic instanceof Shovel) {
            if (HitboxRightClick.rightClicked.get(relic.hb)) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new MethodCallMatcher(AbstractRelic.class, "update");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}




