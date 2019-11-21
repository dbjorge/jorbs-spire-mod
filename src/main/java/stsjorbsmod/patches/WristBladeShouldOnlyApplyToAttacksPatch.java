package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WristBlade;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

// This patch fixes an issue where Wrist Blade is supposed to only apply to attacks, but actually applies to any
// card which deals direct damage. This causes a mistaken interaction with Wanderer's Snake Oil.
public class WristBladeShouldOnlyApplyToAttacksPatch {
    public static boolean shouldSuppressFor(AbstractRelic r, AbstractCard c) {
        return r.relicId.equals(WristBlade.ID) && c.type != AbstractCard.CardType.ATTACK;
    }

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
                    // On the main branch, Wrist Blade is special cased in AbstractCard.
                    //
                    // We handle this by replacing every query for
                    //     AbstractDungeon.player.hasRelic(x)
                    // with
                    //     (AbstractDungeon.player.hasRelic(x) && (!x.equals("WristBlade") || (this.this.type == CardType.ATTACK)))
                    if (methodCall.getClassName().equals(AbstractPlayer.class.getName()) &&
                        methodCall.getMethodName().equals("hasRelic"))
                    {
                        methodCall.replace(String.format(
                                "{ $_ = ($proceed($$) && !%1$s.shouldSuppressFor($1, this)); }",
                                WristBladeShouldOnlyApplyToAttacksPatch.class.getName()));
                    }

                    // On the beta branch, Wrist Blade instead uses a new hook called atDamageGive.
                    //
                    // Ideally, we'd just add a prefix patch to WristBlade.atDamageGive, but we can't do that until
                    // the beta branch releases. In the meantime, we instrument patch the callsite so it can be
                    // ignored on the main branch.
                    if (methodCall.getClassName().equals(AbstractRelic.class.getName()) &&
                        methodCall.getMethodName().equals("atDamageModify"))
                    {
                        methodCall.replace(String.format(
                                "{ $_ = (%1$s.shouldSuppressFor($0, this) ? $1 : $proceed($$)); }",
                                WristBladeShouldOnlyApplyToAttacksPatch.class.getName()));
                    }
                }
            };
        }
    }
}
