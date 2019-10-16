package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;

import java.lang.reflect.InvocationTargetException;

// In addition to the abstract methods, memories are expected to implement a constructor of form
//     new SpecificMemory(AbstractCreature owner, boolean isClarified)
public abstract class AbstractMemory extends AbstractPower implements CloneablePowerInterface {
    private static final String UI_ID = JorbsMod.makeID(AbstractMemory.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    public AbstractCreature source;
    public boolean isClarified;
    public MemoryType memoryType;
    public String baseName; // baseName "Foo" -> name "Memory of Foo"

    private Class<? extends AbstractMemory> leafClass;

    public AbstractMemory(final StaticMemoryInfo staticInfo, final MemoryType memoryType, final AbstractCreature owner, final boolean isClarified) {
        this.ID = staticInfo.ID;
        this.baseName = staticInfo.NAME;
        this.leafClass = staticInfo.CLASS;

        this.owner = owner;
        this.source = owner;
        this.memoryType = memoryType;
        this.isClarified = isClarified;

        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(staticInfo.tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(staticInfo.tex32, 0, 0, 32, 32);

        updateDescription();
    }

    protected void onRemember() {}
    protected void onForget() {}

    protected abstract void updateMemoryDescription();

    @Override
    public final void onInitialApplication() {
        onRemember();
    }

    @Override
    public final void onRemove() {
        if (!isClarified) {
            onForget();
        }
    }

    @Override
    public final void updateDescription() {
        this.updateMemoryDescription();
        this.name = (isClarified ? TEXT[1] : TEXT[0]) + this.baseName;
    }


    @Override
    public AbstractPower makeCopy() {
        try {
            return leafClass.getConstructor(AbstractCreature.class, boolean.class).newInstance(owner, isClarified);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
