package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.BlueCandle;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.powers.CatharsisPower;

public class PlayableCursesPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "canUse"
    )
    public static class AbstractCard_canUse {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractPlayer.class.getName()) &&
                            methodCall.getMethodName().equals("hasRelic")) {
                        methodCall.replace(String.format(
                                "{ $_ = ($proceed($$) || (%1$s.isBlueCandle($1) && %1$s.hasCatharsisPower($0))); }",
                                PlayableCursesPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static boolean isBlueCandle(String blueCandleId) {
        return blueCandleId.equals(BlueCandle.ID);
    }

    public static boolean hasCatharsisPower(AbstractPlayer p) {
        return p.hasPower(CatharsisPower.POWER_ID);
    }


}