package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
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
                    if (methodCall.getClassName().equals(AbstractPlayer.class.getName()) &&
                            methodCall.getMethodName().equals("hasRelic")) {
                        methodCall.replace(String.format(
                                " { $_= ($proceed($$) || (%1$s.isFrozenEye($1) && %1$s.hasClarifyPower())); } ", ClarifyPatchName));
                    }
                }
            };
        }
    }
    public static boolean hasClarifyPower() {
        return AbstractDungeon.player.hasPower(ClarifyPower.POWER_ID);
    }

    public static boolean isFrozenEye(String frozenEyeId) { return frozenEyeId.equals(FrozenEye.ID); }
}
