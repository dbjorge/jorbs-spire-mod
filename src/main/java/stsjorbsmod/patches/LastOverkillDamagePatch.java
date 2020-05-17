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

    public static void setOverkillDamage(AbstractMonster m, int damageAmount) {
        LastOverkillDamageField.lastOverkillDamage.set(m, damageAmount - m.lastDamageTaken);
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
            };
        }
    }
}
