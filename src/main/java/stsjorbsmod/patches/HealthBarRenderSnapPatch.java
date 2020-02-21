package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.powerInterfaces.HealthBarRenderPowerPatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import javassist.CtBehavior;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.FragilePower;

public class HealthBarRenderSnapPatch {
    @SpirePatch(clz = HealthBarRenderPowerPatch.RenderPowerHealthBar.class, method = "Insert")
    public static class RenderSnapHealthBar {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"x", "y", "prevPowerAmtSum", "targetHealthBarWidth", "HEALTH_BAR_HEIGHT", "HEALTH_BAR_OFFSET_Y"}
        )
        public static void Insert(AbstractCreature __instance, SpriteBatch sb, float x, float y, @ByRef int[] prevPowerAmtSum,
                                  float targetHealthBarWidth, float HEALTH_BAR_HEIGHT, float HEALTH_BAR_OFFSET_Y) {
            if (!((__instance instanceof Wanderer) && (((Wanderer) __instance).snapCounter.isSnapTurn())) ||
                    !(__instance.hasPower(FragilePower.POWER_ID) && (((FragilePower) __instance.getPower(FragilePower.POWER_ID)).amount == 1))) {
                return;
            }

            Wanderer p = (Wanderer) __instance;
            Color color = new Color();
            color.r = (MathUtils.cosDeg((float) (System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F;
            color.g = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F;
            color.b = (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F;
            color.a = 1.0F;
            sb.setColor(color);

            int amt = MemoryManager.forPlayer(p).countCurrentClarities() * 3;
            if (amt > 0 && __instance.hasPower(IntangiblePower.POWER_ID)) {
                amt = 1;
            }

            if (__instance.currentHealth > prevPowerAmtSum[0]) {
                float w = 1.0f - (__instance.currentHealth - prevPowerAmtSum[0]) / (float) __instance.currentHealth;
                w *= targetHealthBarWidth;
                if (__instance.currentHealth > 0) {
                    sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                }
                sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, targetHealthBarWidth - w, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.HEALTH_BAR_R, x + targetHealthBarWidth - w, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            }

            prevPowerAmtSum[0] += amt;
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "powers");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
