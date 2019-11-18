package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.Handler;
import javassist.expr.MethodCall;

// This addresses issue #142, where enabling Darklings to flee with Fear causes the fight to become unwinnable
public class DarklingEscapePatch {
    // This makes "fear 1 or 2 darklings, then kill the others" work
    @SpirePatch(
            clz = Darkling.class,
            method = "damage"
    )
    public static class Darkling_damage
    {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall mc) throws CannotCompileException {
                    // replacing the "m.id.equals("Darkling")" check while evaluating whether all darklings are halfDead
                    // already or not with "(m.id.equals("Darkling") && !m.isEscaping)"
                    if (mc.getClassName().equals(String.class.getName()) && mc.getMethodName().equals("equals")) {
                        mc.replace("{ $_ = ($proceed($$) && !m.isEscaping); }");
                    }
                }
            };
        }
    }

    // This makes "fear all darklings simultaneously" and "fear last non-halfDead darkling" work
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "escape"
    )
    public static class AbstractMonster_escape
    {
        @SpirePostfixPatch
        public static void patch(AbstractMonster __this) {
            if (__this.id.equals(Darkling.ID) && AbstractDungeon.getMonsters().monsters.stream().allMatch(m -> m.isDeadOrEscaped())) {
                AbstractDungeon.getCurrRoom().cannotLose = false;
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m.halfDead) {
                        m.halfDead = false;
                        m.die();
                    }
                }
            }
        }
    }
}