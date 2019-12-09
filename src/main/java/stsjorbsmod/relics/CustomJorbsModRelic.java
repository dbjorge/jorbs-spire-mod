package stsjorbsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makeRelicOutlinePath;
import static stsjorbsmod.JorbsMod.makeRelicPath;

public abstract class CustomJorbsModRelic extends CustomRelic {

    private static Texture relicTextureFromId(String id) {
        String unprefixedId = id.replace(JorbsMod.MOD_ID + ":","");
        String path = String.format("%1$sResources/images/relics/%2$s.png", JorbsMod.MOD_ID, unprefixedId);
        return TextureLoader.getTexture(path);
    }

    private static Texture relicOutlineTextureFromId(String id) {
        String unprefixedId = id.replace(JorbsMod.MOD_ID + ":","");
        String path = String.format("%1$sResources/images/relics/generated/%2$s_outline.png", JorbsMod.MOD_ID, unprefixedId);
        return TextureLoader.getTexture(path);
    }

    public CustomJorbsModRelic(final String id, final RelicTier relicTier, final LandingSound landingSound) {
        super(id, relicTextureFromId(id), relicOutlineTextureFromId(id), relicTier, landingSound);
    }
}