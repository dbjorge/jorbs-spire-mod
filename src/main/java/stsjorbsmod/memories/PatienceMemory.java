package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.CoilPower;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

// Gain 1 Coil each time you play a card. When forgetting, deal 2 damage to all enemies for each Coil and lose all Coil.
public class PatienceMemory extends AbstractMemory implements CloneablePowerInterface {
    private static final int COIL_PER_CARD = 1;
    public static final int DAMAGE_PER_COIL_ON_LEAVE = 2;

    public static final String POWER_ID = JorbsMod.makeID(PatienceMemory.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("diligence_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("diligence_memory_power32.png"));

    public PatienceMemory(final AbstractCreature owner, boolean isClarified) {
        super(NAME, MemoryType.VIRTUE, owner, isClarified);
        ID = POWER_ID;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, source, new CoilPower(owner, source, COIL_PER_CARD)));
    }

    @Override
    public void onForget() {
        if (!owner.hasPower(CoilPower.POWER_ID)) {
            return;
        }

        this.flash();

        int numCoil = owner.getPower(CoilPower.POWER_ID).amount;
        int enemyDamage = DAMAGE_PER_COIL_ON_LEAVE * numCoil;

        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(source, DamageInfo.createDamageMatrix(enemyDamage, true), DamageType.THORNS, AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(owner, source, CoilPower.POWER_ID));
    }

    @Override
    public void updateMemoryDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new PatienceMemory(owner, isClarified);
    }
}
