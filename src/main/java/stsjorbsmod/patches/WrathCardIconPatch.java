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
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import stsjorbsmod.util.ReflectionUtils;
import stsjorbsmod.util.RenderUtils;
import stsjorbsmod.util.TextureLoader;

public class WrathCardIconPatch {
    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    public static class AbstractCard_renderEnergy {
        // This is intentionally a little further out than the Energy cost because it can feasibly hit 2-digit numbers
        public static final float WRATH_TEXT_OFFSET_X = 135.0F;
        public static final float WRATH_TEXT_OFFSET_Y = 192.0F;
        public static final Texture wrathIconOverlayTexture = TextureLoader.getTexture("stsjorbsmodResources/images/512/card_wrath_icon_overlay.png");
        public static final AtlasRegion wrathIconOverlayImg = new AtlasRegion(wrathIconOverlayTexture, 0, 0, 512, 512);

        @SpirePostfixPatch
        public static void Postfix(AbstractCard card, SpriteBatch sb) {
            int wrathCount = WrathField.wrathEffectCount.get(card);

            boolean darken = ReflectionUtils.getPrivateField(card, AbstractCard.class, "darken");
            if (wrathCount == 0 || darken || card.isLocked || !card.isSeen) {
                return;
            }

            Color renderColor = ReflectionUtils.getPrivateField(card, AbstractCard.class, "renderColor");
            RenderUtils.renderAtlasRegionCenteredAt(sb, wrathIconOverlayImg, card.current_x, card.current_y, card.drawScale * Settings.scale, renderColor, card.angle);

            Color textColor = Color.WHITE.cpy();
            textColor.a = card.transparency;
            String text = Integer.toString(wrathCount);
            BitmapFont font = FontHelper.cardEnergyFont_L;
            font.getData().setScale(card.drawScale);
            FontHelper.renderRotatedText(sb,
                    font,
                    text,
                    card.current_x,
                    card.current_y,
                    WRATH_TEXT_OFFSET_X * card.drawScale * Settings.scale,
                    WRATH_TEXT_OFFSET_Y * card.drawScale * Settings.scale,
                    card.angle,
                    false,
                    textColor);
        }
    }
    
    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderCost"
    )
    public static class SingleCardViewPopup_renderCost {
        public static final float WRATH_ICON_OFFSET_X = (float)Settings.WIDTH / 2.0F + 270.0F * Settings.scale;
        public static final float WRATH_TEXT_OFFSET_X = (float)Settings.WIDTH / 2.0F + 252.0F * Settings.scale;
        public static final float WRATH_ICON_OFFSET_Y = (float)Settings.HEIGHT / 2.0F + 425.0F * Settings.scale;
        public static final float WRATH_TEXT_OFFSET_Y = (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale;
        public static final Texture wrathIconOverlayTexture = TextureLoader.getTexture("stsjorbsmodResources/images/1024/card_wrath_icon_overlay.png");
        public static final AtlasRegion wrathIconOverlayImg = new AtlasRegion(wrathIconOverlayTexture, 0, 0, 164, 250);

        @SpirePostfixPatch
        public static void Postfix(SingleCardViewPopup __this, SpriteBatch sb) {
            AbstractCard card = ReflectionUtils.getPrivateField(__this, SingleCardViewPopup.class, "card");
            int wrathCount = WrathField.wrathEffectCount.get(card);

            if (wrathCount == 0 || card.isLocked || !card.isSeen) {
                return;
            }

            RenderUtils.renderAtlasRegionCenteredAt(sb, wrathIconOverlayImg, WRATH_ICON_OFFSET_X, WRATH_ICON_OFFSET_Y);

            String text = Integer.toString(wrathCount);
            BitmapFont font = FontHelper.SCP_cardEnergyFont;
            float x = WRATH_TEXT_OFFSET_X;
            if (wrathCount > 9) x -= (20 * Settings.scale);
            FontHelper.renderFont(sb, font, text, x, WRATH_TEXT_OFFSET_Y, Settings.CREAM_COLOR);
        }
    }
}
