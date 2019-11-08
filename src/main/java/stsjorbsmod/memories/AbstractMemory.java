package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.IOnModifyGoldListener;
import stsjorbsmod.util.RenderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// In addition to the abstract methods, memories are expected to implement a constructor of form
//     new SpecificMemory(AbstractCreature owner, boolean isClarified)
public abstract class AbstractMemory implements IOnModifyGoldListener {
    private static final float HB_WIDTH = 64F * Settings.scale;
    private static final float HB_HEIGHT = 64F * Settings.scale;
    private static final float TIP_X_THRESHOLD = 1544.0F * Settings.scale;
    private static final float TIP_OFFSET_R_X = 20.0F * Settings.scale;
    private static final float TIP_OFFSET_L_X = -380.0F * Settings.scale;

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
    public boolean isPassiveEffectActive;
    public boolean isClarified;
    public boolean isRemembered;
    public MemoryType memoryType;

    private ArrayList<AbstractGameEffect> renderEffects = new ArrayList<>();

    private Class<? extends AbstractMemory> leafClass;

    private StaticMemoryInfo staticInfo;

    private static Color ICON_COLOR = Settings.CREAM_COLOR.cpy();
    private float centerX;
    private float centerY;
    private Hitbox hb;

    public AbstractMemory(final StaticMemoryInfo staticInfo, final MemoryType memoryType, final AbstractCreature owner) {
        this.ID = staticInfo.ID;
        this.baseName = staticInfo.NAME;
        this.baseDescription = staticInfo.DESCRIPTIONS[0];
        this.leafClass = staticInfo.CLASS;
        this.staticInfo = staticInfo;

        this.owner = owner;
        this.memoryType = memoryType;
        this.isClarified = false;
        this.isRemembered = false;

        this.hb = new Hitbox(HB_WIDTH, HB_HEIGHT);
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
    public void atStartOfTurnPostDraw() {}
    public void atEndOfTurn(boolean isPlayer) {}
    public float atDamageGive(float originalDamage, DamageType type) { return originalDamage; }
    public float modifyBlock(float originalBlockAmount) { return originalBlockAmount; };
    public void onPlayCard(AbstractCard card, AbstractMonster monster) { }
    // onMonsterDeath can happen within the same action that ends the combat, so you shouldn't queue new actions in here.
    public void onMonsterDeath(AbstractMonster monster) { }
    public void onVictory() { }

    private AbstractPower makeFakePowerForEffects() {
        AbstractPower p = new AbstractPower() {};
        p.region48 = this.staticInfo.CLARITY_IMG_48;
        p.region128 = this.staticInfo.CLARITY_IMG_84;
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

    public void render(SpriteBatch sb) {
        if(isClarified) {
            RenderUtils.renderAtlasRegionCenteredAt(sb, this.staticInfo.CLARITY_IMG_48, centerX, centerY, ICON_COLOR);
        } else {
            RenderUtils.renderAtlasRegionCenteredAt(sb, this.staticInfo.EMPTY_IMG_48, centerX, centerY, ICON_COLOR);
        }
        if (isRemembered) {
            RenderUtils.renderAtlasRegionCenteredAt(sb, this.staticInfo.REMEMBER_IMG_48, centerX, centerY, ICON_COLOR);
        }

        for (AbstractGameEffect effect : renderEffects) {
            effect.render(sb, centerX, centerY);
        }

        if (hb.hovered) {
            ArrayList<PowerTip> tips = new ArrayList<>();
            tips.add(new PowerTip(name, description, staticInfo.CLARITY_IMG_48));

            // Based on the AbstractCreature.renderPowerTips impl
            float tipX = centerX + hb.width / 2.0F < TIP_X_THRESHOLD ?
                    centerX + hb.width / 2.0F + TIP_OFFSET_R_X :
                    centerX - hb.width / 2.0F + TIP_OFFSET_L_X;

            // The calculatedAdditionalOffset ensures everything is shifted to avoid going offscreen
            float tipY = centerY + TipHelper.calculateAdditionalOffset(tips, centerY);

            TipHelper.queuePowerTips(tipX, tipY, tips);
        }
    }

    public void update(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.hb.update(centerX - (HB_WIDTH / 2.0F), centerY - (HB_HEIGHT / 2.0F));

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
