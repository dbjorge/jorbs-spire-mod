package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

// This patch fixes an issue where Wrist Blades is supposed to only apply to attacks, but actually applies to any
// card which deals direct damage. This causes a mistaken interaction with Wanderer's Snake Oil.
//
// We handle this by replacing every query for
//     AbstractDungeon.player.hasRelic(x)
// with
//     (AbstractDungeon.player.hasRelic(x) && (!x.equals("WristBlade") || (this.this.type == CardType.ATTACK)))
public class WriteBladesShouldOnlyApplyToAttacksPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class AbstractCard_commonWristBladesPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractPlayer.class.getName()) && methodCall.getMethodName().equals("hasRelic")) {
                        methodCall.replace("{ $_ = ($proceed($$) && (!$1.equals(\"WristBlade\") || (this.type == com.megacrit.cardcrawl.cards.AbstractCard.CardType.ATTACK))); }");
                    }
                }
            };
        }
    }
}
