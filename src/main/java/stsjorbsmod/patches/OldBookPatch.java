package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.powers.OldBookPower;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class OldBookPatch {

    private static boolean hasOldBook = false;

    public static boolean shouldOldBookTrigger(AbstractPlayer p, int damageAmount) {
        
        return true;
    }

//    public static void resetHpAndHeal(AbstractPlayer p) {
//        p.currentHealth = HpBeforeDamageField.previousHp.get(p);
//
//        AbstractCard c = ((OldBookPower) p.getPower(OldBookPower.POWER_ID)).getCard();
//
//        if (c.upgraded) {
//            float percent = (float)c.magicNumber / 100.0F;
//            int healAmt = (int)((float)p.maxHealth * percent);
//            if (healAmt < 1) {
//                healAmt = 1;
//            }
//
//            p.heal(healAmt, true);
//        }
//    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractPlayer_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName()) &&
                            fieldAccess.getFieldName().equals("lastDamageTaken")) {
                        fieldAccess.replace(String.format("{ if (%1$s.shouldOldBookTrigger($0, damageAmount)) { damageAmount = 0; lastDamageTaken = 0; }" +
                                                            "else { $proceed($$); }; } ",
                                OldBookPatch.class.getName()));
                    }
                }
            };
        }
    }
}
