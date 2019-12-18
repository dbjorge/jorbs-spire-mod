package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class StaticPowerInfo {
    public final Class<? extends AbstractPower> CLASS;
    public final String ID;
    public final String NAME;
    public final String[] DESCRIPTIONS;

    public final Texture TEXTURE_84;
    public final Texture TEXTURE_32;
    public final AtlasRegion IMG_84;
    public final AtlasRegion IMG_32;

    public static <T extends AbstractPower> StaticPowerInfo Load(Class<T> powerClass) {
        return new StaticPowerInfo(powerClass);
    }

    private <T extends AbstractPower> StaticPowerInfo(Class<T> powerClass) {
        CLASS = powerClass;
        ID = JorbsMod.makeID(powerClass.getSimpleName());
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        String imageFilenamePrefix = powerClass.getSimpleName().replace("Power", "");
        TEXTURE_84 = TextureLoader.getTexture(makePowerPath(imageFilenamePrefix + "_84.png"));
        TEXTURE_32 = TextureLoader.getTexture(makePowerPath(imageFilenamePrefix + "_32.png"));

        IMG_84 = new AtlasRegion(TEXTURE_84, 0, 0, 84, 84);
        IMG_32 = new AtlasRegion(TEXTURE_32, 0, 0, 32, 32);
    }
}
