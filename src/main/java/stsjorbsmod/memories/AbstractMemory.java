package stsjorbsmod.memories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
import stsjorbsmod.powers.OnModifyGoldSubscriber;
import stsjorbsmod.tips.MemoryFtueTip;
import stsjorbsmod.util.RenderUtils;
import stsjorbsmod.util.TextureLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static stsjorbsmod.JorbsMod.makeMemoryPath;

// In addition to the abstract methods, memories are expected to implement a constructor of form
//     new SpecificMemory(AbstractCreature owner)
public abstract class AbstractMemory implements OnModifyGoldSubscriber {
    private static final float HB_WIDTH = 64F * Settings.scale;
    private static final float HB_HEIGHT = 64F * Settings.scale;
    private static final float TIP_X_THRESHOLD = 1544.0F * Settings.scale;
    private static final float TIP_OFFSET_R_X = 20.0F * Settings.scale;
    private static final float TIP_OFFSET_L_X = -380.0F * Settings.scale;

    private static final String UI_ID = JorbsMod.makeID(AbstractMemory.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    public static final Texture REMEMBER_BG_TEXTURE_64 = TextureLoader.getTexture(makeMemoryPath("remember_bg_64.png"));
    public static final AtlasRegion REMEMBER_BG_IMG_64 = new AtlasRegion(REMEMBER_BG_TEXTURE_64, 0, 0, 64, 64);
    public static final Color REMEMBER_BG_COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    public static final float REMEMBER_BG_ROTATION_DURATION = 30.0F;

    public String ID;
    public String name;
    // baseDescription "Does foo !M! times" + placeholder "!M!"::"2" -> description "Does foo 2 times"
    private String baseDescription;
    private Map<String, String> descriptionPlaceholders = new HashMap<>();
    public String description;
    public MemoryType memoryType;
    public AbstractCreature owner;
    private StaticMemoryInfo staticInfo;

    public boolean isClarified;
    public boolean isRemembered;
    public boolean isPassiveEffectActive() {
        return isClarified || isRemembered;
    }

    private static Color ICON_COLOR = Settings.CREAM_COLOR.cpy();
    private float centerX;
    private float centerY;
    private Hitbox hb;
    private float rememberBgRotationTimer = 0.0F;
    private ArrayList<AbstractGameEffect> renderEffects = new ArrayList<>();

    public AbstractMemory(final StaticMemoryInfo staticInfo, final MemoryType memoryType, final AbstractCreature owner) {
        this.ID = staticInfo.ID;
        this.name = staticInfo.NAME;
        this.baseDescription = staticInfo.DESCRIPTIONS[0];
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
    public void onGainPassiveEffect() {}
    // This is triggered when a memory is forgotten *or* a memory/clarity is snapped
    // Unlike onForget, this is *not* triggered when forgetting a memory you already have clarity of
    public void onLosePassiveEffect() {}
    // This is triggered when a memory is forgotten/snapped, but *not* when a clarity is snapped
    public void onForget() {}

    // These are the specific subset of power hooks required by Memory implementations.
    // ** Be sure to check isPassiveEffectActive as necessary. **
    @Override public void onModifyGold(AbstractPlayer p) {}
    public void atStartOfTurnPostDraw() {}
    public void atEndOfTurnPreEndTurnCards() {}
    public void atEndOfTurn(boolean isPlayer) {}
    public void onPlayCard(AbstractCard card, AbstractMonster monster) { }
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) { }
    // onMonsterDeath can happen within the same action that ends the combat, so you shouldn't queue new actions in here.
    public void onMonsterKilled(AbstractMonster monster) { }
    public void onVictory() { }
    public void onEnergyRecharge() { }

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
        if (isRemembered || MemoryFtueTip.shouldFakeBeingRemembered(this)) {
            float rotation = (-rememberBgRotationTimer / REMEMBER_BG_ROTATION_DURATION) * 360F;
            RenderUtils.renderAtlasRegionCenteredAt(sb, REMEMBER_BG_IMG_64, centerX, centerY, Settings.scale, REMEMBER_BG_COLOR, rotation);
        }

        AtlasRegion img = isClarified || MemoryFtueTip.shouldFakeBeingClarified(this) ?
                this.staticInfo.CLARITY_IMG_48 :
                this.staticInfo.EMPTY_IMG_48;

        RenderUtils.renderAtlasRegionCenteredAt(sb, img, centerX, centerY, ICON_COLOR);

        for (AbstractGameEffect effect : renderEffects) {
            effect.render(sb, centerX, centerY);
        }

        if (!AbstractDungeon.isScreenUp && hb.hovered) {
            renderTip();
        }
    }

    protected void addExtraPowerTips(ArrayList<PowerTip> tips) { }

    private void renderTip() {
        ArrayList<PowerTip> tips = new ArrayList<>();
        tips.add(new PowerTip(name, description, staticInfo.CLARITY_IMG_48));
        addExtraPowerTips(tips);

        // Based on the AbstractCreature.renderPowerTips impl
        float tipX = centerX + hb.width / 2.0F < TIP_X_THRESHOLD ?
                centerX + hb.width / 2.0F + TIP_OFFSET_R_X :
                centerX - hb.width / 2.0F + TIP_OFFSET_L_X;

        // The calculatedAdditionalOffset ensures everything is shifted to avoid going offscreen
        float tipY = centerY + TipHelper.calculateAdditionalOffset(tips, centerY);

        TipHelper.queuePowerTips(tipX, tipY, tips);
    }

    public void update(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.hb.update(centerX - (HB_WIDTH / 2.0F), centerY - (HB_HEIGHT / 2.0F));

        this.updateRememberBgRotationTimer();

        Iterator i = this.renderEffects.iterator();
        while(i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }
    }

    private void updateRememberBgRotationTimer() {
        this.rememberBgRotationTimer -= Gdx.graphics.getDeltaTime();
        if (this.rememberBgRotationTimer < 0.0F) {
            this.rememberBgRotationTimer = REMEMBER_BG_ROTATION_DURATION;
        }
    }

    protected final void setDescriptionPlaceholder(String placeholder /* eg, !M! */, Object value) {
        descriptionPlaceholders.put(placeholder, value.toString());
        updateDescription();
    }

    public final void updateDescription() {
        String prefix =
                (isClarified && isRemembered) ? TEXT[0] + TEXT[1] :
                isClarified ? TEXT[0] :
                isRemembered ? TEXT[1] :
                /* neither: */ TEXT[2];

        this.description = prefix + " NL " + applyPlaceholderDictionary(this.baseDescription, this.descriptionPlaceholders);
    }

    private String applyPlaceholderDictionary(String string, Map<String, String> placeholders) {
        for(Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            string = string.replace(placeholder.getKey(), placeholder.getValue());
        }
        return string;
    }

    public AbstractMemory makeCopy() {
        try {
            return staticInfo.CLASS.getConstructor(AbstractCreature.class).newInstance(owner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
