package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import stsjorbsmod.util.ReflectionUtils;

public class RenderBurningBlockPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
    public static class AbstractCreature_renderBlockIconAndValue {
        @SpirePrefixPatch
        public static void patch(AbstractCreature __this, SpriteBatch sb, float x, float y) {
            sb.setColor(Color.GOLD.cpy());
            sb.draw(ImageMaster.ATK_FIRE,
                    x + (Float)ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "BLOCK_ICON_X") - 32.0F,
                    y + (Float)ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "BLOCK_ICON_Y") - 32.0F + (Float)ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "blockOffset"),
                    32.0F,
                    32.0F,
                    64.0F,
                    64.0F,
                    Settings.scale,
                    Settings.scale,
                    0.0F);
        }
    }
}
