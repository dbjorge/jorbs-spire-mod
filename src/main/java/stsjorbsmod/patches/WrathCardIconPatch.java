package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import stsjorbsmod.util.ReflectionUtils;
import stsjorbsmod.util.TextureLoader;

@SpirePatch(
        clz = AbstractCard.class,
        method = "renderEnergy"
)
public class WrathCardIconPatch {
    private static final float WRATH_TEXT_OFFSET_X = 132.0F;
    private static final float WRATH_TEXT_OFFSET_Y = 192.0F;
    private static final Texture wrathIconOverlayTexture = TextureLoader.getTexture("stsjorbsmodResources/images/512/card_wrath_icon_overlay.png");
    private static final AtlasRegion wrathIconOverlayImg = new AtlasRegion(wrathIconOverlayTexture, 0, 0, 512, 512);

    @SpirePostfixPatch
    public static void Postfix(AbstractCard __this, SpriteBatch sb) {
        int wrathCount = WrathField.wrathEffectCount.get(__this);

        boolean darken = ReflectionUtils.getPrivateField(__this, AbstractCard.class, "darken");
        if (wrathCount == 0 || darken || __this.isLocked || !__this.isSeen) {
            return;
        }

        Color renderColor = ReflectionUtils.getPrivateField(__this, AbstractCard.class, "renderColor");
        sb.setColor(renderColor);
        sb.draw(wrathIconOverlayImg,
                __this.current_x + wrathIconOverlayImg.offsetX - (float)wrathIconOverlayImg.originalWidth / 2.0F,
                __this.current_y + wrathIconOverlayImg.offsetY - (float)wrathIconOverlayImg.originalHeight / 2.0F,
                (float)wrathIconOverlayImg.originalWidth / 2.0F - wrathIconOverlayImg.offsetX,
                (float)wrathIconOverlayImg.originalHeight / 2.0F - wrathIconOverlayImg.offsetY,
                (float)wrathIconOverlayImg.packedWidth,
                (float)wrathIconOverlayImg.packedHeight,
                __this.drawScale * Settings.scale,
                __this.drawScale * Settings.scale,
                __this.angle);

        Color textColor = Color.WHITE.cpy();
        textColor.a = __this.transparency;
        String text = Integer.toString(wrathCount);
        BitmapFont font = FontHelper.cardEnergyFont_L;
        font.getData().setScale(__this.drawScale);
        FontHelper.renderRotatedText(sb,
                font,
                text,
                __this.current_x,
                __this.current_y,
                WRATH_TEXT_OFFSET_X * __this.drawScale * Settings.scale,
                WRATH_TEXT_OFFSET_Y * __this.drawScale * Settings.scale,
                __this.angle,
                false,
                textColor);
    }
}
