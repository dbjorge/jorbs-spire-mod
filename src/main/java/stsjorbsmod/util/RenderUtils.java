package stsjorbsmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;

public class RenderUtils {
    public static final Color UNMODIFIED_COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    public static final float UNMODIFIED_SCALE = Settings.scale;
    public static final float UNMODIFIED_ROTATION = 0.0F;

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y) {
        renderAtlasRegionCenteredAt(sb, atlasRegion, x, y, UNMODIFIED_SCALE, UNMODIFIED_COLOR, UNMODIFIED_ROTATION);
    }

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y, float scale) {
        renderAtlasRegionCenteredAt(sb, atlasRegion, x, y, scale, UNMODIFIED_COLOR, UNMODIFIED_ROTATION);
    }

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y, Color color) {
        renderAtlasRegionCenteredAt(sb, atlasRegion, x, y, UNMODIFIED_SCALE, color, UNMODIFIED_ROTATION);
    }

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y, float scale, Color color) {
        renderAtlasRegionCenteredAt(sb, atlasRegion, x, y, scale, color, UNMODIFIED_ROTATION);
    }

    public static void renderAtlasRegionCenteredAt(SpriteBatch sb, TextureAtlas.AtlasRegion atlasRegion, float x, float y, float scale, Color color, float rotation) {
        sb.setColor(color);
        sb.draw(
                atlasRegion,
                x + atlasRegion.offsetX - (float)atlasRegion.packedWidth / 2.0F,
                y + atlasRegion.offsetY - (float)atlasRegion.packedHeight / 2.0F,
                (float)atlasRegion.packedWidth / 2.0F - atlasRegion.offsetX,
                (float)atlasRegion.packedHeight / 2.0F - atlasRegion.offsetY,
                (float)atlasRegion.packedWidth,
                (float)atlasRegion.packedHeight,
                scale,
                scale,
                rotation);
    }
}
