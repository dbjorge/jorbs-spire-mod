package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makeMemoryPath;

public class StaticMemoryInfo {
    public final Class<? extends AbstractMemory> CLASS;
    public final String ID;
    public final String NAME;
    public final String[] DESCRIPTIONS;
    
    public final Texture CLARITY_TEXTURE_84;
    public final Texture CLARITY_TEXTURE_48;
    public final Texture EMPTY_TEXTURE_84;
    public final Texture EMPTY_TEXTURE_48;
    public final Texture REMEMBER_TEXTURE_84;
    public final Texture REMEMBER_TEXTURE_48;

    public final AtlasRegion CLARITY_IMG_84;
    public final AtlasRegion CLARITY_IMG_48;
    public final AtlasRegion EMPTY_IMG_84;
    public final AtlasRegion EMPTY_IMG_48;
    public final AtlasRegion REMEMBER_IMG_84;
    public final AtlasRegion REMEMBER_IMG_48;

    public static <T extends AbstractMemory> StaticMemoryInfo Load(Class<T> memoryClass) {
        return new StaticMemoryInfo(memoryClass);
    }

    private <T extends AbstractMemory> StaticMemoryInfo(Class<T> memoryClass) {
        CLASS = memoryClass;
        ID = JorbsMod.makeID(memoryClass.getSimpleName());
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        String imageFilenamePrefix = memoryClass.getSimpleName().replace("Memory","");
        CLARITY_TEXTURE_84 = TextureLoader.getTexture(makeMemoryPath("c" + imageFilenamePrefix + "_84.png"));
        CLARITY_TEXTURE_48 = TextureLoader.getTexture(makeMemoryPath("c" + imageFilenamePrefix + "_48.png"));
        EMPTY_TEXTURE_84 = TextureLoader.getTexture(makeMemoryPath("e" + imageFilenamePrefix + "_84.png"));
        EMPTY_TEXTURE_48 = TextureLoader.getTexture(makeMemoryPath("e" + imageFilenamePrefix + "_48.png"));
        REMEMBER_TEXTURE_84 = TextureLoader.getTexture(makeMemoryPath("r" + imageFilenamePrefix + "_84.png"));
        REMEMBER_TEXTURE_48 = TextureLoader.getTexture(makeMemoryPath("r" + imageFilenamePrefix + "_48.png"));

        CLARITY_IMG_84 = new TextureAtlas.AtlasRegion(CLARITY_TEXTURE_84, 0, 0, 84, 84);
        CLARITY_IMG_48 = new TextureAtlas.AtlasRegion(CLARITY_TEXTURE_48, 0, 0, 48, 48);
        EMPTY_IMG_84 = new TextureAtlas.AtlasRegion(EMPTY_TEXTURE_84, 0, 0, 84, 84);
        EMPTY_IMG_48 = new TextureAtlas.AtlasRegion(EMPTY_TEXTURE_48, 0, 0, 48, 48);
        REMEMBER_IMG_84 = new TextureAtlas.AtlasRegion(REMEMBER_TEXTURE_84, 0, 0, 84, 84);
        REMEMBER_IMG_48 = new TextureAtlas.AtlasRegion(REMEMBER_TEXTURE_48, 0, 0, 48, 48);
    }
}
