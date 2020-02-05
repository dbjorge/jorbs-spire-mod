package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.powers.CustomJorbsModPower;

public class AtStartOfTurnPreLoseBlockPatch {
    public static void applyStartOfTurnPreLoseBlockPowers() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            for (AbstractPower p : m.powers) {
                if (p instanceof CustomJorbsModPower) {
                    ((CustomJorbsModPower) p).atStartOfTurnPreLoseBlock();
                }
            }
        }
    }

    @SpirePatch(clz = EndTurnAction.class, method = "update")
    public static class AbstractRoom_endTurn {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractDungeon.class.getName()) && f.getFieldName().equals("topLevelEffects")) {
                        f.replace(String.format("{ %1$s.applyStartOfTurnPreLoseBlockPowers(); $_ = $proceed(); }",
                                AtStartOfTurnPreLoseBlockPatch.class.getName()));
                    }
                }
            };
        }
    }
}