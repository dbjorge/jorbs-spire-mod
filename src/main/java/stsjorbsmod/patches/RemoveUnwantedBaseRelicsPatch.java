package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.relics.WingBoots;
import stsjorbsmod.characters.Cull;

public class RemoveUnwantedBaseRelicsPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeRelicList"
    )
    public static class Implementation
    {
        @SpirePrefixPatch
        public static void patch(AbstractDungeon __instance)
        {
            AbstractDungeon.relicsToRemoveOnStart.add(PrismaticShard.ID);

            if (__instance.player instanceof Cull) {
                AbstractDungeon.relicsToRemoveOnStart.add(WingBoots.ID); // Cull has this implicitly
            }
        }
    }
}