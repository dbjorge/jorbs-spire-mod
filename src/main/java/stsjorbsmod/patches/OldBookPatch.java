package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class OldBookPatch {

    public static boolean hasOldBook(AbstractPlayer p) {
        return true;
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractMonster_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName()) &&
                            fieldAccess.getFieldName().equals("isDead")) {
                        fieldAccess.replace(String.format("{ if (%1$s.hasOldBook($0)) { currentHealth = 50; return; }; " +
                                "$proceed($$); }", OldBookPatch.class.getName()));
                    }
                }
            };
        }
    }
}
