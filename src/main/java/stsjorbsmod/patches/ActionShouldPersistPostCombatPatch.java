package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.HealAction;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;

// Replaces "e instanceof HealAction" with "shouldPersistPostCombat(e) || (e instanceof HealAction)"
@SpirePatch(clz = GameActionManager.class, method = "clearPostCombatActions")
public class ActionShouldPersistPostCombatPatch {
    public static boolean shouldPersistPostCombat(Object action) {
        return ActionShouldPersistPostCombatField.shouldPersistPostCombat.get(action);
    }

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(Instanceof instanceOf) throws CannotCompileException {
                try {
                    if (instanceOf.getType().getName().equals(HealAction.class.getName())) {
                        instanceOf.replace(String.format(
                                "{ $_ = (%1$s.shouldPersistPostCombat($1) || $proceed($$)); }",
                                ActionShouldPersistPostCombatPatch.class.getName()));
                    }
                } catch (NotFoundException e) {
                    throw new CannotCompileException(e);
                }
            }
        };
    }
}





