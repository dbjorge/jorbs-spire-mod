package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import stsjorbsmod.util.ReflectionUtils;

import java.util.UUID;

@SpirePatch(
        clz = ModifyBlockAction.class,
        method = "update"
)
public class FixModifyBlockActionCardDescriptionPatch {
    @SpirePostfixPatch
    public static void Postfix(ModifyBlockAction __this) {
        UUID uuid = ReflectionUtils.getPrivateField(__this, ModifyBlockAction.class, "uuid");
        for (AbstractCard instance : GetAllInBattleInstances.get(uuid)) {
            instance.applyPowers();
            instance.initializeDescription();
        }
    }
}




