package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.IOnModifyGoldListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// In addition to the abstract methods, memories are expected to implement a constructor of form
//     new SpecificMemory(AbstractCreature owner, boolean isClarified)
public abstract class AbstractMemory implements IOnModifyGoldListener {
    private static final String UI_ID = JorbsMod.makeID(AbstractMemory.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    public String ID;
    // baseName "Foo" -> name "Memory of Foo" / "Clarity of Foo"
    public String baseName;
    public String name;
    // baseDescription "Does foo !M! times" + placeholder "!M!"::"2" -> description "Does foo 2 times"
    private String baseDescription;
    private Map<String, String> descriptionPlaceholders = new HashMap<>();
    public String description;

    public AbstractCreature owner;
    public boolean isClarified;
    public boolean isPassiveEffectActive;
    public MemoryType memoryType;

    public TextureAtlas.AtlasRegion region128;
    public TextureAtlas.AtlasRegion region48;
    private ArrayList<AbstractGameEffect> renderEffects = new ArrayList<>();

    private Class<? extends AbstractMemory> leafClass;

    public AbstractMemory(final StaticMemoryInfo staticInfo, final MemoryType memoryType, final AbstractCreature owner, final boolean isClarified) {
        this.ID = staticInfo.ID;
        this.baseName = staticInfo.NAME;
        this.baseDescription = staticInfo.DESCRIPTIONS[0];
        this.leafClass = staticInfo.CLASS;

        this.owner = owner;
        this.memoryType = memoryType;
        this.isClarified = isClarified;
        this.isPassiveEffectActive = false;

        this.region128 = new TextureAtlas.AtlasRegion(staticInfo.tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(staticInfo.tex32, 0, 0, 32, 32);

        updateDescription();
    }

    // This is triggered when a memory is remembered but *not* when a clarity is gained
    public void onRemember() {}
    // This is triggered when a memory is remembered *or* a clarity is gained
    // Unlike onRemember, this is *not* triggered when remembering a memory you already have clarity of
    public final void gainPassiveEffect() { this.isPassiveEffectActive = true; this.onGainPassiveEffect(); }
    protected void onGainPassiveEffect() {}
    // This is triggered when a memory is forgotten *or* a memory/clarity is snapped
    // Unlike onForget, this is *not* triggered when forgetting a memory you already have clarity of
    public final void losePassiveEffect() { this.isPassiveEffectActive = false; this.onLosePassiveEffect(); }
    protected void onLosePassiveEffect() {}
    // This is triggered when a memory is forgotten/snapped, but *not* when a clarity is snapped
    public void onForget() {}

    // These are the specific subset of power hooks required by Memory implementations.
    // ** Be sure to check isPassiveEffectActive as necessary. **
    @Override public void onModifyGold(AbstractPlayer p) {}
    public void atEndOfTurn(boolean isPlayer) {}
    public float atDamageGive(float originalDamage, DamageType type) { return originalDamage; }
    public void onPlayCard(AbstractCard card, AbstractMonster monster) { }
    public void onNonMinionMonsterDeath() { }
    public void onVictory() { }

    private AbstractPower makeFakePowerForEffects() {
        AbstractPower p = new AbstractPower() {};
        p.region48 = this.region48;
        p.region128 = this.region128;
        p.owner = this.owner;
        return p;
    }

    public void flash() {
        AbstractPower p = makeFakePowerForEffects();
        this.renderEffects.add(new GainPowerEffect(p));
        AbstractDungeon.effectList.add(new FlashPowerEffect(p));
    }

    public void flashWithoutSound() {
        AbstractPower p = makeFakePowerForEffects();
        this.renderEffects.add(new SilentGainPowerEffect(p));
        AbstractDungeon.effectList.add(new FlashPowerEffect(p));
    }

    public void render(SpriteBatch sb, float x, float y, Color color) {
        sb.setColor(color);
        sb.draw(this.region48, x - (float)this.region48.packedWidth / 2.0F, y - (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth / 2.0F, (float)this.region48.packedHeight / 2.0F, (float)this.region48.packedWidth, (float)this.region48.packedHeight, Settings.scale, Settings.scale, 0.0F);

        for (AbstractGameEffect effect : renderEffects) {
            effect.render(sb, x, y);
        }
    }

    public void update() {
        Iterator i = this.renderEffects.iterator();
        while(i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }
    }

    protected final void setDescriptionPlaceholder(String placeholder /* eg, !M! */, Object value) {
        descriptionPlaceholders.put(placeholder, value.toString());
        updateDescription();
    }

    public final void updateDescription() {
        this.description = applyPlaceholderDictionary(this.baseDescription, this.descriptionPlaceholders);
        this.name = (isClarified ? TEXT[1] : TEXT[0]) + this.baseName;
    }

    private String applyPlaceholderDictionary(String string, Map<String, String> placeholders) {
        for(Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            string = string.replace(placeholder.getKey(), placeholder.getValue());
        }
        return string;
    }

    public AbstractMemory makeCopy() {
        try {
            return leafClass.getConstructor(AbstractCreature.class, boolean.class).newInstance(owner, isClarified);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
