package stsjorbsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

public abstract class CustomJorbsModRelic extends CustomRelic {
    public final CardColor relicColor;

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

    public CustomJorbsModRelic(final String id, final CardColor relicColor, final RelicTier relicTier, final LandingSound landingSound) {
        super(id, relicTextureFromId(id), relicOutlineTextureFromId(id), relicTier, landingSound);
        this.relicColor = relicColor;
    }


    @Override
    public void initializeTips() {
        this.description = DESCRIPTIONS[0];
        super.initializeTips();
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replaceAll(JorbsMod.MOD_ID + ":", "#y");
    }
}