package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.characters.Cull;


@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock"
)
public class CullBlockPatch {
    public CullBlockPatch() {}

    @SpirePrefixPatch
    public static SpireReturn patch(AbstractCreature __instance, int blockAmount) {
        if(!(__instance instanceof Cull)) { return SpireReturn.Continue(); }
        AbstractDungeon.actionManager.addToBottom(
                new DamageRandomEnemyAction(
                        new DamageInfo(__instance, blockAmount, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
        );
        return SpireReturn.Return(null);
    }
}
