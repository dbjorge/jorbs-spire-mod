package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import javassist.CtBehavior;
import stsjorbsmod.cards.cull.Withering;

public class WitheringPatch {
    @SpirePatch(clz = IntangiblePlayerPower.class, method = "atDamageFinalReceive")
    public static class ReduceIntangibleDamageReductionPatch
    {
        @SpireInsertPatch(rloc = 4)
        public static void patch(IntangiblePlayerPower __instance, float dmg, DamageInfo.DamageType type, @ByRef String[] damage) {
            if(AbstractDungeon.player.hasPower("stsjorbsmod:WitherPower") && dmg > 1)
            {
                damage[0] += 1;
            }
        }
    }
}
