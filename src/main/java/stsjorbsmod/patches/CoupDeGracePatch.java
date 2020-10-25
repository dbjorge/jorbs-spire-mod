package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.powers.CoupDeGracePower;
import stsjorbsmod.powers.WitheringPower;

public class CoupDeGracePatch {
    public static final String CoupDeGracePatchName = CoupDeGracePatch.class.getName();

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class AbstractMonster_damage {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __this, DamageInfo info) {
            if (shouldCalculatePreventedDamage(__this) && !TrueDamagePatch.TrueDamageInfoField.isTrueDamage.get(info)) {
                increasePreventedDamage(__this, info);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "calculateDamage"
    )
    public static class AbstractMonster_calculateDamage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.getFieldName().equals("intentDmg") &&
                            fa.isWriter()) {
                        fa.replace(String.format("{ if (%1$s.shouldCalculatePreventedDamage(target)) { this.intentDmg = %1$s.getIntangibleDamageAmount(target, dmg); }  else { $_ = $proceed($$); } }", CoupDeGracePatchName));
                    }
                }
            };
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
                increasePreventedDamage(__this, info);
            }
        }
    }

    @SpirePatch(
            clz = Nemesis.class,
            method = "damage" // Nemesis has a special damage() that sets damage to 1 based on the non-player "Intangible" power
    )
    public static class Nemesis_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fa) throws CannotCompileException {
                    // suppress the initial Intangible effect on info.output
                    if (fa.getClassName().equals(DamageInfo.class.getName()) &&
                            fa.getFieldName().equals("output") &&
                            fa.isWriter()) {
                        fa.replace(String.format("{ if (%1$s.shouldCalculatePreventedDamage(this)) { %1$s.increasePreventedDamage(this, $0); $_ = $proceed($$); } }", CoupDeGracePatchName));
                    }
                }
            };
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
                return SpireReturn.Continue();
            }
            return SpireReturn.Return(damage);
        }
    }

    @SpirePatch(
            clz = IntangiblePower.class,
            method = "atDamageFinalReceive"
    )
    public static class IntangiblePower_atDamageFinalReceive {
        @SpirePrefixPatch
        public static SpireReturn patch(IntangiblePower __this, float damage, DamageInfo.DamageType type) {
            if (!shouldCalculatePreventedDamage(__this.owner)) {
                return SpireReturn.Continue();
            }
            return SpireReturn.Return(damage);
        }
    }

    public static boolean shouldCalculatePreventedDamage(AbstractCreature c) {
        return ((c.hasPower(IntangiblePlayerPower.POWER_ID) || c.hasPower(IntangiblePower.POWER_ID)) && c.hasPower(CoupDeGracePower.POWER_ID) && c.currentBlock <= 0);
    }

    public static void increasePreventedDamage(AbstractCreature c, DamageInfo info) {
        CoupDeGracePower po = (CoupDeGracePower)c.getPower(CoupDeGracePower.POWER_ID);
        po.increaseBaseOutputDamage(info.output, c.currentBlock);
    }

    public static int getIntangibleDamageAmount(AbstractCreature c, int dmg) {
        if (dmg <= 0)
            return 0;
        else if (c.hasPower(WitheringPower.POWER_ID))
            return Math.min(dmg, 1 + c.getPower(WitheringPower.POWER_ID).amount);
        else {
            return 1;
        }
    }
}