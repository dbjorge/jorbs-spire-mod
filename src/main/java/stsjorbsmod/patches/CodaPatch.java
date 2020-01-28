package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class CodaPatch {
    public static SpireField<Integer> hpRetained = new SpireField<>(() -> 0);

    public static void retainHP(AbstractCreature __this, DamageInfo info) {
        if (__this.currentHealth <= 0 && hpRetained.get(info) > 0) {
            __this.currentHealth = hpRetained.get(info);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class AbstractMonster_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor(){
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractMonster.class.getName()) && methodCall.getMethodName().equals("healthBarUpdatedEvent")) {
                        methodCall.replace("{ " + CodaPatch.class.getName() + ".retainHP(this, info); $_ = $proceed(); }");
                    }
                }
            };
        }
    }
}
