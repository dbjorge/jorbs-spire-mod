package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

/**
 * Setting this field to true in an AbstractGameAction ctor will result in that action being immune to
 * GameActionManager.clearPostCombatActions. Use it for actions with permanent post-combat effects that
 * are intended to occur even if a damage action ends the combat while the action is enqueued.
 */
@SpirePatch(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
public class ActionShouldPersistPostCombatField {
    public static SpireField<Boolean> shouldPersistPostCombat = new SpireField<>(() -> false);
}
