package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class StaticMemoryInfo {
    public final Class<? extends AbstractMemory> CLASS;
    public final String ID;
    public final String NAME;
    public final String[] DESCRIPTIONS;
    public final Texture tex84;
    public final Texture tex32;

    public static <T extends AbstractMemory> StaticMemoryInfo Load(Class<T> memoryClass) {
        return new StaticMemoryInfo(memoryClass);
    }

    private <T extends AbstractMemory> StaticMemoryInfo(Class<T> memoryClass) {
        CLASS = memoryClass;
        ID = JorbsMod.makeID(memoryClass.getSimpleName());
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
        tex84 = TextureLoader.getTexture(makePowerPath(memoryClass.getSimpleName() + "_84.png"));
        tex32 = TextureLoader.getTexture(makePowerPath(memoryClass.getSimpleName() + "_32.png"));
    }
}
