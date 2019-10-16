package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.PermanentlyIncreaseCardDamageAction;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class WrathMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int DAMAGE_INCREASE_PER_KILL = 1;

    public static final String POWER_ID = JorbsMod.makeID(WrathMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("wrath_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("wrath_memory_power32.png"));

    public WrathMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.SIN, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (card.type != AbstractCard.CardType.ATTACK || target.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        card.calculateCardDamage(target);
        if (card.damage >= target.currentHealth) {
            JorbsMod.logger.info("Wrath: increasing damage");
            AbstractDungeon.actionManager.addToTop(
                    new PermanentlyIncreaseCardDamageAction(card.uuid, DAMAGE_INCREASE_PER_KILL));
        }
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + DAMAGE_INCREASE_PER_KILL + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new WrathMemory(owner, isClarified);
    }
}
