package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class FindFamiliarPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(FindFamiliarPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("find_familiar_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("find_familiar_power32.png"));

    private int cardsRetainedAfterSnapped;

    public FindFamiliarPower(final AbstractCreature owner, final int cardsRetainedAfterSnapped) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;
        this.cardsRetainedAfterSnapped = cardsRetainedAfterSnapped;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        MemoryManager mm = MemoryManager.forPlayer(owner);
        if (isPlayer && mm != null) {
            AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(owner));
            if (cardsRetainedAfterSnapped > 0 && mm.isSnapped()) {
                AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(owner, cardsRetainedAfterSnapped));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + cardsRetainedAfterSnapped + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FindFamiliarPower(owner, cardsRetainedAfterSnapped);
    }
}
