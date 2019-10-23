package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import java.util.Iterator;
import stsjorbsmod.cards.DimensionDoor;

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
            if (__result || !DimensionDoor.isMapTravelActive()) {
                return __result;
            }

            // Any room on the next level is actually reachable.
            Iterator edgeIterator = __this.getEdges().iterator();

            while(edgeIterator.hasNext()) {
                MapEdge edge = (MapEdge)edgeIterator.next();

                if (__node.y == edge.dstY) {
                    return true;
                }
            }

            return false;
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
            if (DimensionDoor.isMapTravelActive()) {
                DimensionDoor.deactivateMapTravel();
            }
        }
    }
}