package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.relics.WingBoots;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.Wanderer;

public class RemoveUnwantedBaseRelicsPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeRelicList"
    )
    public static class Implementation
    {
        @SpirePrefixPatch
        public static void patch(AbstractDungeon __instance) {
            boolean cull = false;
            if (__instance.player instanceof Wanderer || (cull = __instance.player instanceof Cull)) {
                AbstractDungeon.relicsToRemoveOnStart.add(PrismaticShard.ID);

                // Cull implicitly has Wing Boots
                if (cull) { AbstractDungeon.relicsToRemoveOnStart.add(WingBoots.ID); }
            }
        }
    }
}