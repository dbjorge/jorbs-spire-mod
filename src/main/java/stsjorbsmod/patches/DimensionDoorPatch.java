package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import stsjorbsmod.potions.DimensionDoorPotion;

public class DimensionDoorPatch {
    @SpirePatch(
            clz = MapRoomNode.class,
            method = "isConnectedTo"
    )
    public static class MapRoomNode_isConnectedTo
    {
        @SpirePostfixPatch
        public static boolean patch(boolean __result, MapRoomNode __this, MapRoomNode __node)
        {
            if (__result || !DimensionDoorPotion.isMapTravelActive()) {
                return __result;
            }

            // Any room on the next level, or up to a range beyond that, is actually reachable.
            return __this.getEdges().stream().anyMatch(
                    edge -> (edge.dstY <= __node.y)
                            && (__node.y <= edge.dstY + DimensionDoorPotion.getMapTravelRange())
            );
        }
    }

    @SpirePatch(
            clz = AbstractScene.class,
            method = "nextRoom"
    )
    public static class AbstractScene_nextRoom
    {
        @SpirePostfixPatch
        public static void patch(AbstractScene __this, AbstractRoom __room) {
            // Only let this ability be used once.
            if (DimensionDoorPotion.isMapTravelActive()) {
                DimensionDoorPotion.deactivateMapTravel();
            }
        }
    }
}