package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
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
import java.util.HashSet;
import java.util.Set;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class IntrospectionPower extends TwoAmountPower implements CloneablePowerInterface, OnModifyMemoriesListener, CustomStackBehaviorPower {
    public int baseDamage;
    public int damagePerClarity;

    public static final String POWER_ID = JorbsMod.makeID(IntrospectionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("introspection_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("introspection_power32.png"));

    public IntrospectionPower(final AbstractCreature owner, final int loseHpAmount, final int baseDamage, final int damagePerClarity) {
        ID = POWER_ID;
        this.name = NAME;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.owner = owner;

        this.amount2 = loseHpAmount;
        this.baseDamage = baseDamage;
        this.damagePerClarity = damagePerClarity;

        recalculateAmount();
        updateDescription();
    }

    private void recalculateAmount() {
        amount = baseDamage + damagePerClarity * MemoryManager.forPlayer(owner).countCurrentClarities();
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, amount2));
            int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(owner, damageMatrix, DamageType.THORNS, AttackEffect.FIRE));
        }
    }

    @Override
    public void onModifyMemories() {
        recalculateAmount();
        updateDescription();
    }

    @Override
    public MemoryManager.MemoryEventType[] getMemoryEventTypes() {
        return MemoryManager.MemoryEventType.values();
    }

    @Override
    public void stackPower(AbstractPower otherIntrospectionPower) {
        IntrospectionPower other = (IntrospectionPower) otherIntrospectionPower;
        this.amount2 += other.amount2;
        this.baseDamage += other.baseDamage;
        this.damagePerClarity += other.damagePerClarity;

        recalculateAmount();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.damagePerClarity + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new IntrospectionPower(owner, amount2, baseDamage, damagePerClarity);
    }
}

