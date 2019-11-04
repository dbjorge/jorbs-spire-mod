package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

import static stsjorbsmod.JorbsMod.makePowerPath;

public class IntrospectionPower extends AbstractPower implements CloneablePowerInterface, OnModifyMemoriesListener {
    public int loseHpAmount;
    public int baseDamage;
    public int damagePerClarity;
    public DamageType damageType;

    public static final String POWER_ID = JorbsMod.makeID(IntrospectionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("introspection_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("introspection_power32.png"));

    public IntrospectionPower(final AbstractCreature owner, final int loseHpAmount, final int baseDamage, final int damagePerClarity, final DamageType damageType) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;

        this.loseHpAmount = loseHpAmount;
        this.baseDamage = baseDamage;
        this.damagePerClarity = damagePerClarity;
        this.damageType = damageType;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    private int calculateDamage() {
        return baseDamage + damagePerClarity * MemoryManager.forPlayer(owner).countCurrentClarities();
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, loseHpAmount));
            int[] damageMatrix = DamageInfo.createDamageMatrix(calculateDamage(), damageType != DamageType.NORMAL);
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(owner, damageMatrix, damageType, AttackEffect.FIRE));
        }
    }

    @Override
    public void onModifyMemories() {
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.loseHpAmount + DESCRIPTIONS[1] + calculateDamage() + DESCRIPTIONS[2] + this.damagePerClarity + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new IntrospectionPower(owner, loseHpAmount, baseDamage, damagePerClarity, damageType);
    }
}

