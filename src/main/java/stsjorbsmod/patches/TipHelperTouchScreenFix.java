package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

// This patch fixes the symptom that, in touchscreen mode, tooltips never show up for the AbstractMemory UI components.
// The actual issue is that TipHelper won't display tips if the player is in touchscreen mode and the mouse is within
// the "isHoveringDropZone" band where cards/potions can be dropped onto monsters/players to target them; even if nothing
// is currently being dragged. This patch updates TipHelper to only omit tips within that bar if something is being
// dragged (a card/potion).
public class TipHelperTouchScreenFix {
    public static boolean isInHoverTargetMode(AbstractPlayer player) {
        return player.isDraggingCard || player.hoveredCard != null || AbstractDungeon.topPanel.potionUi.targetMode;
    }

    @SpirePatch(clz = TipHelper.class, method = "render")
    public static class ShowCardAndAddToDiscardEffect_update {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractPlayer.class.getName()) && f.getFieldName().equals("isHoveringDropZone")) {
                        f.replace(String.format("{" +
                                        "$_ = (%1$s.isInHoverTargetMode($0) && $proceed());" +
                                        "}",
                                TipHelperTouchScreenFix.class.getName()));
                    }
                }
            };
        }
    }
}

