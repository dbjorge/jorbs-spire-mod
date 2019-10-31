package stsjorbsmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;

public class RenderUtils {
    public static final Color UNMODIFIED_COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y) {
        renderAtlasRegionCenteredAt(sb, atlasRegion, x, y, UNMODIFIED_COLOR);
    }

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y, Color color) {
        sb.setColor(color);
        sb.draw(
                atlasRegion,
                x - (float)atlasRegion.packedWidth / 2.0F,
                y - (float)atlasRegion.packedHeight / 2.0F,
                (float)atlasRegion.packedWidth / 2.0F,
                (float)atlasRegion.packedHeight / 2.0F,
                (float)atlasRegion.packedWidth,
                (float)atlasRegion.packedHeight,
                Settings.scale,
                Settings.scale,
                0.0F);
    }
}
