package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.stats.StatsScreen;
import javassist.CtBehavior;
import stsjorbsmod.characters.Cull;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ManifestPatch {

    public static final Logger logger = LogManager.getLogger(ManifestPatch.class.getName());

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

    @SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = { SaveFile.class })
    public static class AbstractDungeon_nextRoomTransition
    {
        // Note: it's important that this happen *before* the saveIfAppropriate call on the following line
        @SpireInsertPatch(locator = IncrementFloorClimbedLocator.class)
        public static void updateFloorNumPatch(AbstractDungeon __this, SaveFile saveFile) {
            if (AbstractDungeon.player != null) {
                logger.info("Current Floor: " + __this.getCurrMapNode().y );
                logger.info("Next Floor: " + __this.nextRoom.y );
                logger.info("Current Floor Count: " + __this.floorNum );
                
                /*The chest rooms after the boss are the same y so the formula evaluates to -1 preventing increment without this if
                  Every act except act 3 puts you at y=-1 before the first room, but act 3 is y=15 resulting in a reset of floor count without the if
                  The Ending has an issue where boss rooms are always y=15 so it goes from y=2 to y=15 unless you stop the formula from running
                  */
                if(__this.nextRoom.y>__this.getCurrMapNode().y && !(__this instanceof TheEnding)){
                //Otherwise increment floor count by the difference between Current y level and next y level -1 to account for the game's increment by 1
                __this.floorNum += __this.nextRoom.y-__this.getCurrMapNode().y-1;
                }

                boolean nextRoomIsEvent = __this.nextRoom != null && __this.nextRoom.room instanceof EventRoom;
                int startingManifest = (nextRoomIsEvent && AbstractDungeon.player instanceof Cull) ? 1 : 0;
                PlayerManifestField.manifestField.set(AbstractDungeon.player, startingManifest);
            }
        }

        private static class IncrementFloorClimbedLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                final Matcher matcher = new Matcher.MethodCallMatcher(StatsScreen.class, "incrementFloorClimbed");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}