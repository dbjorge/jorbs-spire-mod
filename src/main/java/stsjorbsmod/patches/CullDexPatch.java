package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;
import stsjorbsmod.characters.Cull;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCreature.class,
                AbstractCreature.class,
                AbstractPower.class,
                int.class,
                boolean.class,
                AbstractGameAction.AttackEffect.class
        }
)
public class CullDexPatch {
    public CullDexPatch() {}

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            public void edit(FieldAccess f) throws CannotCompileException {
                if(f.getFieldName().equals("powerToApply") && f.isWriter()) {
                    f.replace(String.format(
                            "{$0.powerToApply = %1$s.updateDexPower($1);}",
                            CullDexPatch.class.getName()));
                }
            }
        };
    }

    public static AbstractPower updateDexPower(AbstractPower power) {
        if (power instanceof  DexterityPower && AbstractDungeon.player instanceof Cull && power.owner instanceof Cull) {
            return new StrengthPower(power.owner, power.amount);
        } else {
            return power;
        }
    }
}


