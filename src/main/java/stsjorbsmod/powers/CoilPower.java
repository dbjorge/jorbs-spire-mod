package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesListener;
import stsjorbsmod.util.TextureLoader;

import java.util.Collections;

import static stsjorbsmod.JorbsMod.makePowerPath;

// When you forget Patience, deal 2 damage to all enemies for each coil and lose all coil.
public class CoilPower extends AbstractPower implements CloneablePowerInterface, OnModifyMemoriesListener {
    public AbstractCreature source;

    public static final String POWER_ID = JorbsMod.makeID(CoilPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public static final int DAMAGE_PER_COIL = 1;

    public CoilPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    private int calculateDamage() {
        return amount * DAMAGE_PER_COIL;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + calculateDamage() + DESCRIPTIONS[1];
    }

    @Override
    public void onModifyMemories() {
        this.flash();

        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(calculateDamage(), true), DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL));
        AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(owner, owner, CoilPower.POWER_ID));
    }

    @Override
    public MemoryManager.MemoryEventType[] getMemoryEventTypes() {
        return new MemoryManager.MemoryEventType[]{MemoryManager.MemoryEventType.REMEMBER};
    }

    @Override
    public AbstractPower makeCopy() {
        return new CoilPower(owner, source, amount);
    }
}
