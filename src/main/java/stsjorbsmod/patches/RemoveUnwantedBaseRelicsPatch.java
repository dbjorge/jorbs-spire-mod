package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.relics.WingBoots;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.Wanderer;

public class RemoveUnwantedBaseRelicsPatch {
    @SpirePatch(clz = AbstractDungeon.class, method = "initializeRelicList")
    public static class Implementation
    {
        @SpirePrefixPatch
        public static void patch(AbstractDungeon __instance) {
            boolean cull = __instance.player instanceof Cull;
            boolean wanderer = __instance.player instanceof Wanderer;

            if (wanderer || cull) {
                AbstractDungeon.relicsToRemoveOnStart.add(PrismaticShard.ID);
            }

            if (cull) {
                AbstractDungeon.relicsToRemoveOnStart.add(WingBoots.ID); // CULL has this implicitly
            }
        }
    }
}