package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.powers.CoupDeGracePower;

public class CoupDeGracePatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class AbstractMonster_damage {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __this, DamageInfo info) {
            if (shouldCalculatePreventedDamage(__this) && !TrueDamagePatch.TrueDamageInfoField.isTrueDamage.get(info)) {
                CoupDeGracePower po = (CoupDeGracePower)__this.getPower(CoupDeGracePower.POWER_ID);
                po.increaseBaseOutputDamage(info.output, __this.currentBlock);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class AbstractPlayer_damage {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __this, DamageInfo info) {
            if (shouldCalculatePreventedDamage(__this) && !TrueDamagePatch.TrueDamageInfoField.isTrueDamage.get(info)) {
                CoupDeGracePower po = (CoupDeGracePower)__this.getPower(CoupDeGracePower.POWER_ID);
                po.increaseBaseOutputDamage(info.output, __this.currentBlock);
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

    public static boolean shouldCalculatePreventedDamage(AbstractCreature m) {
        return (m.hasPower("IntangiblePlayer") && m.hasPower(CoupDeGracePower.POWER_ID) && m.currentBlock <= 0);
    }
}


