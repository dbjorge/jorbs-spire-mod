package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import stsjorbsmod.powers.CoupDeGracePower;

public class CoupDeGracePatch {
    public static boolean shouldCalculatePreventedDamage(AbstractCreature m) {
        return (m.hasPower("IntangiblePlayer") && m.hasPower(CoupDeGracePower.POWER_ID));
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class AbstractMonster_damage {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __this, DamageInfo info) {
            if (shouldCalculatePreventedDamage(__this)) {
                CoupDeGracePower.increaseBaseOutputDamage(info.output, __this.currentBlock);
            }
        }
    }

    @SpirePatch(
            clz = IntangiblePlayerPower.class,
            method = "atDamageFinalReceive"
    )
    public static class IntangiblePlayerPower_atDamageFinalReceive {
        @SpirePrefixPatch
        public static SpireReturn patch(IntangiblePlayerPower __this, float damage, DamageInfo.DamageType type) {
            if (!shouldCalculatePreventedDamage(__this.owner)) {
                SpireReturn.Continue();
            }
            return SpireReturn.Return(damage);
        }
    }
}


