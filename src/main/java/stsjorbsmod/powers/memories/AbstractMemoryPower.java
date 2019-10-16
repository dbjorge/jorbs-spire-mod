package stsjorbsmod.powers.memories;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;

// This primarily acts as a marker class, eg so RememberMemoryAction can identify other Memories to remove
public abstract class AbstractMemoryPower extends AbstractPower {
    private static final String UI_ID = JorbsMod.makeID(AbstractMemoryPower.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    public AbstractCreature source;
    public boolean isClarified;
    public MemoryType memoryType;
    public String baseName; // baseName "Foo" -> name "Memory of Foo"

    public AbstractMemoryPower(final String baseName, final MemoryType memoryType, final AbstractCreature owner, final boolean isClarified) {
        this.baseName = baseName;

        this.owner = owner;
        this.source = owner;
        this.memoryType = memoryType;
        this.isClarified = isClarified;

        type = PowerType.BUFF;
        isTurnBased = false;
    }

    @Override
    public final void updateDescription() {
        this.updateMemoryDescription();
        this.name = (isClarified ? TEXT[1] : TEXT[0]) + this.baseName;
    }

    public void updateMemoryDescription() { }
}
