package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.util.CardUtils;

@SpirePatch(clz = BottledFlame.class, method = "onEquip")
@SpirePatch(clz = BottledLightning.class, method = "onEquip")
@SpirePatch(clz = BottledTornado.class, method = "onEquip")
public class BottledRelic_onEquipPatch {

    public static class GetBottleableCardsExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(CardGroup.class.getName()) && methodCall.getMethodName().equals("getPurgeableCards")) {
                methodCall.replace(String.format("{ $_ = %1$s.getCardsForBottling($proceed($$)); }", CardUtils.class.getName()));
            }
        }
    }

    public static ExprEditor Instrument() {
        return new GetBottleableCardsExprEditor();
    }
}
