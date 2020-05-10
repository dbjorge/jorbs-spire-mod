package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DrawPileViewScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.powers.ClarifyPower;

public class ClarifyPatch {
    public static final String ClarifyPatchName = ClarifyPatch.class.getName();

    @SpirePatch(clz = DrawPileViewScreen.class, method = "open")
    @SpirePatch(clz = DrawPileViewScreen.class, method = "render")
    public static class AbstractRoomPatch {
        public static ExprEditor Instrument() {
            final int[] counter = {0};
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getMethodName().equals("hasRelic")) {
                        if (counter[0] == 0) {
                            methodCall.replace(
                                    "$_= $proceed($$) || "+ClarifyPatchName+".playerHasPower();"
                            );
                        }
                        counter[0]++;
                    }
                }
            };
        }
    }
    public static boolean playerHasPower() {
        return AbstractDungeon.player.hasPower(ClarifyPower.POWER_ID);
    }
}
