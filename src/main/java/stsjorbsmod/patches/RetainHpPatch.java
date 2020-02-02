package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class RetainHpPatch {
    public static SpireField<Integer> hpRetained = new SpireField<>(() -> 0);

    public static int retainHP(AbstractCreature __this, DamageInfo info, int damageAmount) {
        if (hpRetained.get(info) > 0 && damageAmount >= __this.currentHealth) {
            damageAmount = __this.currentHealth - hpRetained.get(info);
            // in the event we want to allow for retaining more than 1HP on targets, this should support that.
            if (damageAmount < 0) {
                damageAmount = 0;
            }
        }
        return damageAmount;
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class AbstractMonster_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(AbstractMonster.class.getName()) && fieldAccess.getFieldName().equals("lastDamageTaken")) {
                        fieldAccess.replace("{ damageAmount = " + RetainHpPatch.class.getName() + ".retainHP(this, info, damageAmount); $proceed($$); }");
                    }
                }
            };
        }
    }
}
