package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import stsjorbsmod.characters.Cull;

public class ManifestPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class PlayerManifestField {
        public static SpireField<Integer> manifestField = new SpireField<>(() -> 0);
    }

    // CULL has a permanent Wing Boots effect, and also skips floors based on manifest
    // built up during combat. This patch combines those effects by using manifest to
    // determine which floor ("Y") we'll be going to and then allowing CULL to treat any
    // room at that Y level as "connected" for purposes of map traversal.
    @SpirePatch(clz = MapRoomNode.class, method = "isConnectedTo")
    public static class MapRoomNode_isConnectedTo
    {
        @SpirePostfixPatch
        public static boolean patch(boolean originalResult, MapRoomNode __this, MapRoomNode __node)
        {
            if (AbstractDungeon.player == null) {
                return originalResult;
            }

            int manifest = PlayerManifestField.manifestField.get(AbstractDungeon.player);

            if (manifest == 0 && !(AbstractDungeon.player instanceof Cull)) {
                return originalResult;
            }

            int startingY = __this.y;
            int targetY = startingY + 1 + manifest;
            // Mapping has special handling for the first floor of each act that breaks if they're ever considered connected-to
            if (__node.y == 0) {
                return false;
            }
            // Don't skip mid-act treasure chests
            if (startingY < 8 && targetY > 8) {
                targetY = 8;
            }
            // Don't skip boss fires
            else if (startingY < 14 && targetY > 14) {
                targetY = 14;
            }
            // Don't skip bosses
            else if (startingY < 15 && targetY > 15) {
                targetY = 15;
            }

            return __node.y == targetY;
        }
    }

    // This resets manifest after each room movement
    @SpirePatch(clz = AbstractScene.class, method = "nextRoom")
    public static class AbstractScene_nextRoom
    {
        @SpirePostfixPatch
        public static void patch(AbstractScene __this, AbstractRoom __room) {
            if (!CardCrawlGame.loadingSave && AbstractDungeon.player != null) {
                PlayerManifestField.manifestField.set(AbstractDungeon.player, 0);
            }
        }
    }
}