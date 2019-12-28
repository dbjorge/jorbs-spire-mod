package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import stsjorbsmod.characters.Cull;

public class CullManifestPatch {
    // CULL has a permanent Wing Boots effect
    @SpirePatch(clz = MapRoomNode.class, method = "isConnectedTo")
    public static class MapRoomNode_isConnectedTo
    {
        @SpirePostfixPatch
        public static boolean patch(boolean originalResult, MapRoomNode __this, MapRoomNode __node)
        {
            if (AbstractDungeon.player instanceof Cull) {
                return __node.y == __this.y + 1;
            }
            return originalResult;
        }
    }
}