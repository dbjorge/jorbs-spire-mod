package stsjorbsmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import stsjorbsmod.util.ReflectionUtils;
import stsjorbsmod.util.RenderUtils;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;
import static stsjorbsmod.JorbsMod.makeCardPath;

public class CardLegendaryFramePatch {
    public static final Texture legendaryFrameTexture512 = TextureLoader.getTexture(makeCardPath("common_card_bgs/legendary_frame_512.png"));
    public static final TextureAtlas.AtlasRegion legendaryFrameImg512 = new TextureAtlas.AtlasRegion(legendaryFrameTexture512, 0, 0, 512, 512);

    public static final Texture legendaryFrameTexture1024 = TextureLoader.getTexture(makeCardPath("common_card_bgs/legendary_frame_1024.png"));
    public static final TextureAtlas.AtlasRegion legendaryFrameImg1024 = new TextureAtlas.AtlasRegion(legendaryFrameTexture1024, 0, 0, 1024, 1024);

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderPortraitFrame"
    )
    public static class AbstractCard_renderBannerImage {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractCard __this, SpriteBatch sb, float x, float y) {
            if (__this.hasTag(LEGENDARY)) {
                RenderUtils.renderAtlasRegionCenteredAt(
                        sb,
                        legendaryFrameImg512,
                        x,
                        y,
                        __this.drawScale * Settings.scale,
                        ReflectionUtils.getPrivateField(__this, AbstractCard.class, "renderColor"),
                        __this.angle);

                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = SingleCardViewPopup.class,
            method = "renderFrame"
    )
    public static class SingleCardViewPopup_renderFrame {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SingleCardViewPopup __this, SpriteBatch sb) {
            AbstractCard card = ReflectionUtils.getPrivateField(__this, SingleCardViewPopup.class, "card");
            if (card.hasTag(LEGENDARY)) {
                RenderUtils.renderAtlasRegionCenteredAt(
                        sb,
                        legendaryFrameImg1024,
                        (float)Settings.WIDTH / 2.0F,
                        (float)Settings.HEIGHT / 2.0F);

                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}