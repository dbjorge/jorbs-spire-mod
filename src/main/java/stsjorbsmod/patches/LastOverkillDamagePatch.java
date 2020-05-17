package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.ReapAndSowSaveData;

public class LastOverkillDamagePatch {
    public static final String LastOverkillDamagePatchName = LastOverkillDamagePatch.class.getName();

    private static AbstractMonster overkillMonster;

    public static void setOverkillDamage(AbstractMonster m, int damageAmount) {
        LastOverkillDamageField.lastOverkillDamage.set(m, damageAmount - m.lastDamageTaken);
        overkillMonster = m;
    }

    public static void updateSaveDataOnFinalKill(DamageInfo info) {
        if (((Cull) info.owner).cardInUse != null  && ((Cull) info.owner).cardInUse.name.equals("Reap and Sow"))
            ReapAndSowSaveData.reapAndSowDamage += LastOverkillDamageField.lastOverkillDamage.get(overkillMonster);
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class AbstractMonster_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.getFieldName().equals("lastDamageTaken") && fa.isWriter())
                    {
                        fa.replace(String.format("{ $_ = $proceed($$); %1$s.setOverkillDamage($0, damageAmount); }", LastOverkillDamagePatchName));
                    }
                }

//                @Override
//                public void edit(MethodCall mc) throws CannotCompileException {
//                    String method = mc.getMethodName();
//
//                    // if we're here, this attack killed the last enemy, check to see if it's Reap and Sow and update save data directly.
//                    if (method.equals("cleanCardQueue"))
//                    {
//                        mc.replace(String.format("{ %1$s.updateSaveDataOnFinalKill(info); $_ = $proceed($$); }", LastOverkillDamagePatchName));
//                        return;
//                    }
//                }
            };
        }
    }
}
