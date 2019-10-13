package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainMemoryClarityAction;
import stsjorbsmod.cards.DefaultRareAttack;
import stsjorbsmod.util.TextureLoader;

// This primarily acts as a marker class, eg so RememberMemoryAction can identify other Memories to remove
public abstract class AbstractMemoryPower extends AbstractPower {
    private static final String UI_ID = JorbsMod.makeID(AbstractMemoryPower.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    public AbstractCreature source;
    public boolean isClarified;
    public String baseName; // baseName "Foo" -> name "Memory of Foo"

    public AbstractMemoryPower(final String baseName, final AbstractCreature owner, final AbstractCreature source, final boolean isClarified) {
        this.baseName = baseName;

        this.owner = owner;
        this.source = source;
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
