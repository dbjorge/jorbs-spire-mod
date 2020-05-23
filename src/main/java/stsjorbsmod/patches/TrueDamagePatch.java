package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class TrueDamagePatch {
    public static final String TrueDamagePatchName = TrueDamagePatch.class.getName();

    public static boolean isTrueDamage(DamageInfo info) {
        return TrueDamageInfoField.isTrueDamage.get(info);
    }
    public static boolean isTrueDamage(AbstractCard card) {
        return TrueDamageCardField.isTrueDamage.get(card);
    }

    @SpirePatch(
            clz = DamageInfo.class,
            method = SpirePatch.CLASS
    )
    public static class TrueDamageInfoField {
        @SuppressWarnings("unchecked")
        public static SpireField<Boolean> isTrueDamage = new SpireField(() -> false);
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class TrueDamageCardField {
        @SuppressWarnings("unchecked")
        public static SpireField<Boolean> isTrueDamage = new SpireField(() -> false);
    }

    public static float updateDamage(DamageInfo info, int originalDamage, int updatedDamage) {
        if (isTrueDamage(info) && originalDamage > updatedDamage) {
            return originalDamage;
        } else {
            return updatedDamage;
        }
    }

    public static float updateDamage(AbstractCard card, float originalDamage, float updatedDamage) {
        if (isTrueDamage(card) && originalDamage > updatedDamage) {
            return originalDamage;
        } else {
            return updatedDamage;
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "decrementBlock"
    )
    public static class AbstractCreature_decrementBlock {
        @SpirePrefixPatch
        public static SpireReturn<Integer> patch(AbstractCreature __this, DamageInfo info, int damageAmount) {
            if (isTrueDamage(info)) {
                return SpireReturn.Return(damageAmount);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractPlayer_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall mc) throws CannotCompileException {
                    String cls = mc.getClassName();
                    String method = mc.getMethodName();

                    if (method.equals("hasPower")) {
                        mc.replace(String.format("{ $_ = $proceed($$) && !%1$s.isTrueDamage(info); }", TrueDamagePatchName));
                    }

                    // clamp the onAttackToChangeDamage, onAttackedToChangeDamage, and onAttacked calls
                    if ((cls.equals(AbstractPower.class.getName()) || cls.equals(AbstractRelic.class.getName())) &&
                            (method.equals("onAttackToChangeDamage") || method.equals("onAttackedToChangeDamage") || method.equals("onAttacked")))
                    {
                        mc.replace(String.format("{ $_ = %1$s.updateDamage($1, $2, $proceed($$)); }", TrueDamagePatchName));
                        return;
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    @SpirePatch(clz = Nemesis.class, method = "damage") // Nemesis has a special damage() that sets damage to 1 based on the non-player "Intangible" power
    public static class AbstractMonster_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fa) throws CannotCompileException {
                    // suppress the initial Intangible effect on info.output
                    if (fa.getClassName().equals(DamageInfo.class.getName()) &&
                        fa.getFieldName().equals("output") &&
                        fa.isWriter())
                    {
                        fa.replace(String.format("{ if (!%1$s.isTrueDamage($0)) { $_ = $proceed($$); } }", TrueDamagePatchName));
                    }
                }

                @Override
                public void edit(MethodCall mc) throws CannotCompileException {
                    String cls = mc.getClassName();
                    String method = mc.getMethodName();

                    // clamp the onAttackToChangeDamage, onAttackedToChangeDamage, and onAttacked calls
                    if ((cls.equals(AbstractPower.class.getName()) || cls.equals(AbstractRelic.class.getName())) &&
                        (method.equals("onAttackToChangeDamage") || method.equals("onAttackedToChangeDamage") || method.equals("onAttacked")))
                    {
                        mc.replace(String.format("{ $_ = %1$s.updateDamage($1, $2, $proceed($$)); }", TrueDamagePatchName));
                        return;
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class AbstractCard_commonCalculateMethods {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall mc) throws CannotCompileException {
                    String cls = mc.getClassName();
                    String method = mc.getMethodName();

                    // Clamp all the damage modifier hooks with first parameter of "float tmp"
                    if ((
                            cls.equals(AbstractRelic.class.getName()) ||
                            cls.equals(AbstractPower.class.getName()) ||
                            cls.equals(AbstractStance.class.getName())
                        ) && (
                            method.equals("atDamageModify") ||
                            method.equals("atDamageGive") ||
                            method.equals("atDamageReceive") ||
                            method.equals("atDamageFinalGive") ||
                            method.equals("atDamageFinalReceive")
                    )) {
                        mc.replace(String.format("{ $_ = %1$s.updateDamage(this, $1, $proceed($$)); }", TrueDamagePatchName));
                    }
                }
            };
        }
    }
}
