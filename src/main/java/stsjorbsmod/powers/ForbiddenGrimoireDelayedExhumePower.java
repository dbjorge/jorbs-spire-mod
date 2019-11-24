package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ExhumeCardsAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryManager.MemoryEventType;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class ForbiddenGrimoireDelayedExhumePower extends AbstractPower implements CloneablePowerInterface, OnModifyMemoriesSubscriber {
    public AbstractCreature source;

    public static final String POWER_ID = JorbsMod.makeID(ForbiddenGrimoireDelayedExhumePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private final AbstractCard cardToExhume;

    private static long instanceCounter = 0;

    public ForbiddenGrimoireDelayedExhumePower(final AbstractCreature owner, final AbstractCard cardToExhume, final int cardPlaysUntilExhume) {
        // This prevents the power from stacking with other instances of itself for different card instances.
        // This is the same strategy used by TheBombPower.
        //
        // StSLib provides a NonStackablePower interface with similar functionality, but we're intentionally not using
        // it because it is hackier than the ID thing.
        ID = POWER_ID + "__" + (++instanceCounter);

        name = NAME;

        this.owner = owner;
        this.amount = cardPlaysUntilExhume;
        this.source = owner;

        this.cardToExhume = cardToExhume;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (MemoryManager.forPlayer().isSnapped()) {
            return; // should already be getting removed by a different action in the queue
        }

        if (amount <= 1) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ExhumeCardsAction(cardToExhume));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
        }
    }


    @Override
    public void onModifyMemories() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public MemoryEventType[] getMemoryEventTypes() {
        return new MemoryEventType[] { MemoryEventType.SNAP };
    }

    @Override
    public void updateDescription() {
        description =
                cardToExhume.name +
                DESCRIPTIONS[0] +
                amount +
                (amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ForbiddenGrimoireDelayedExhumePower(owner, cardToExhume, amount);
    }
}
