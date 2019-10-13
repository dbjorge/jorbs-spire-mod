package stsjorbsmod.powers.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class PrideMemoryPower extends AbstractMemoryPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(PrideMemoryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("pride_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("pride_memory_power32.png"));

    public PrideMemoryPower(final AbstractCreature owner, final AbstractCreature source, boolean isClarified) {
        super(NAME, MemoryType.SIN, owner, source, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onVictory() {
        CardGroup masterDeckCandidates = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                masterDeckCandidates.addToBottom(c);
            }
        }

        if (!masterDeckCandidates.isEmpty()) {
            AbstractCard masterDeckCard = masterDeckCandidates.getRandomCard(AbstractDungeon.cardRandomRng);
            masterDeckCard.upgrade();
            masterDeckCard.superFlash();
        }
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PrideMemoryPower(owner, source, isClarified);
    }
}
