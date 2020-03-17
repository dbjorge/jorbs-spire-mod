package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import javassist.CtBehavior;
import stsjorbsmod.cards.cull.Withering;
import stsjorbsmod.powers.WitherPower;

public class WitheringPatch {
    @SpirePatch(clz = IntangiblePlayerPower.class, method = "atDamageFinalReceive")
    public static class ReduceIntangibleDamageReductionPatch
    {
        @SpirePrefixPatch
        public static SpireReturn patch(IntangiblePlayerPower __instance, float dmg, DamageInfo.DamageType type) {
            if(AbstractDungeon.player.hasPower(WitherPower.POWER_ID) && dmg > 1)
            {
               return SpireReturn.Return(2.0f);
            }
            return SpireReturn.Continue();
        }
    }
    //WHY IS INTANGIBLE HARDCODED IN THE ABSTRACT PLAYER CLASS POEIAÖRL KELKR ÖLÖLREK ÖOLKE
    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class ReduceIntangibleDamageRudctionInPlayerDamageMethod
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
        public static void patch(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
            if(AbstractDungeon.player.hasPower(WitherPower.POWER_ID) && info.output > 1)
            {
                damageAmount[0]++;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return found;
            }
        }
    }
}
