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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.DefaultRareAttack;
import stsjorbsmod.util.TextureLoader;

// This primarily acts as a marker class, eg so RememberMemoryAction can identify other Memories to remove
public abstract class AbstractMemoryPower extends AbstractPower {
    public AbstractCreature source;
    public boolean isClarified;

    public AbstractMemoryPower(final AbstractCreature owner, final AbstractCreature source) {
        this.owner = owner;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;
        isClarified = false;
    }
}
