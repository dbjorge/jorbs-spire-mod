package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

// This is for the sake of using Fear on the SpireShield, to enable it to walk left instead of right to escape
public class AbstractMonsterEscapeLeftPatch {
    public static boolean isLeftOfPlayer(AbstractCreature c) {
        return c.drawX < AbstractDungeon.player.drawX;
    }

    @SpirePatch(clz = AbstractMonster.class, method = "updateEscapeAnimation")
    public static class AbstractMonster_updateEscapeAnimation
    {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(Settings.class.getName()) && f.getFieldName().equals("scale")) {
                        f.replace(String.format("{" +
                                "$_ = $proceed() * (%1$s.isLeftOfPlayer(this) ? -1.0F : 1.0F);" +
                                "}",
                                AbstractMonsterEscapeLeftPatch.class.getName()));
                    }
                }
            };
        }
    }
}