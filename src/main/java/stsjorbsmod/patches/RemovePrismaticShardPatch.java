package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;

public class RemovePrismaticShardPatch {
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
        }
    }
}