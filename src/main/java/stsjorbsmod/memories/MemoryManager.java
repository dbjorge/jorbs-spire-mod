package stsjorbsmod.memories;

import basemod.patches.com.megacrit.cardcrawl.relics.AbstractRelic.FixLargeImageRender;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.CoilPower;
import stsjorbsmod.powers.SnappedPower;
import stsjorbsmod.relics.MindglassRelic;
import stsjorbsmod.util.RenderUtils;
import stsjorbsmod.util.TextureLoader;

import java.util.ArrayList;
import java.util.Comparator;

import static stsjorbsmod.JorbsMod.makePowerPath;

// If a memory and a clarity of the same type are active at the same type, the *clarity* owns maintaining the passive
// effect, and the memory only contributes onRemember/onForget effects.
public class MemoryManager {
    private static final float MEMORY_HB_WIDTH = 64F * Settings.scale;
    private static final float MEMORY_HB_HEIGHT = 64F * Settings.scale;
    private static final float CLARITY_PADDING_Y = 32.0F * Settings.scale;
    private static final float MAX_CLARITIES_PER_STACK = 7;
    private static final float CLARITY_HB_WIDTH = 64F * Settings.scale;
    private static final float CLARITY_HB_HEIGHT = CLARITY_PADDING_Y * MAX_CLARITIES_PER_STACK;
    private static final float TIP_X_THRESHOLD = 1544.0F * Settings.scale;
    private static final float TIP_OFFSET_R_X = 20.0F * Settings.scale;
    private static final float TIP_OFFSET_L_X = -380.0F * Settings.scale;

    private static final Color MEMORY_BACKGROUND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.35F);
    private static final Texture MEMORY_BACKGROUND_TEXTURE32 = TextureLoader.getTexture(makePowerPath("thoughtbubble32.png"));
    private static final TextureAtlas.AtlasRegion MEMORY_BACKGROUND_REGION32 = new TextureAtlas.AtlasRegion(MEMORY_BACKGROUND_TEXTURE32, 0, 0, 77, 85);

    private float drawX;
    private float drawY;
    private final float currentMemoryOffsetY;
    private final Hitbox sinClarityStackHb;
    private final Hitbox virtueClarityStackHb;
    private final Hitbox currentMemoryHb;
    private final Wanderer owner;

    public AbstractMemory currentMemory;

    // We maintain these separately because rendering needs them separated and that's the hot path.
    private final ArrayList<AbstractMemory> sinClarities = new ArrayList<>();
    private final ArrayList<AbstractMemory> virtueClarities = new ArrayList<>();

    public MemoryManager(Wanderer owner, float drawX, float drawY, float currentMemoryOffsetY, float clarityOffsetY, float sinOffsetX, float virtueOffsetX) {
        this.owner = owner;
        this.drawX = drawX;
        this.drawY = drawY;
        this.currentMemoryOffsetY = currentMemoryOffsetY;

        this.sinClarityStackHb = new Hitbox(
                drawX + sinOffsetX,
                drawY + clarityOffsetY,
                CLARITY_HB_WIDTH,
                CLARITY_HB_HEIGHT);

        this.virtueClarityStackHb = new Hitbox(
                drawX + virtueOffsetX,
                drawY + clarityOffsetY,
                CLARITY_HB_WIDTH,
                CLARITY_HB_HEIGHT);

        this.currentMemoryHb = new Hitbox(
                drawX - (MEMORY_HB_WIDTH / 2.0F),
                drawY + currentMemoryOffsetY - (MEMORY_HB_HEIGHT / 2.0F),
                MEMORY_HB_WIDTH,
                MEMORY_HB_HEIGHT);
    }

    public static MemoryManager forPlayer(AbstractCreature target) {
        if (target instanceof Wanderer) {
            return ((Wanderer)target).memories;
        }
        return null;
    }

    public void rememberMemory(AbstractMemory memoryToRemember) {
        if (isSnapped()) {
            flashSnap();
        } else if (memoryToRemember.isClarified) {
            gainClarity(memoryToRemember);
        } else if (currentMemory != null && currentMemory.ID.equals(memoryToRemember.ID)) {
            currentMemory.flashWithoutSound();
        } else {
            forgetCurrentMemoryNoNotify();

            this.currentMemory = memoryToRemember;

            this.currentMemory.onRemember();
            if (!hasClarity(currentMemory.ID)) {
                this.currentMemory.gainPassiveEffect();
            }

            AbstractPower possibleCoilPower = this.owner.getPower(CoilPower.POWER_ID);
            if (possibleCoilPower != null) {
                possibleCoilPower.onSpecificTrigger();
            }

            this.currentMemory.flash();
            notifyModifyMemories();
        }
    }

    public void forgetCurrentMemory() {
        forgetCurrentMemoryNoNotify();
        notifyModifyMemories();
    }

    private void forgetCurrentMemoryNoNotify() {
        if (this.currentMemory != null) {
            if (!this.hasClarity(currentMemory.ID)) {
                this.currentMemory.losePassiveEffect();
            }
            this.currentMemory.onForget();
            this.currentMemory = null;
        }
    }

    public void gainClarityOfCurrentMemory() {

        if (currentMemory != null) {
            gainClarity(currentMemory.makeCopy());
        }
    }

    public void gainClarity(AbstractMemory newClarity) {
        if (isSnapped()) {
            flashSnap();
            return;
        }

        if (hasClarity(newClarity.ID)) {
            getClarity(newClarity.ID).flashWithoutSound();
            return;
        }

        boolean passiveAlreadyActive = currentMemory.ID.equals(newClarity.ID);
        if (passiveAlreadyActive) {
            // The current memory's passive effect will already be active, but we want to transition ownership of the
            // passive effect to the new clarity so it isn't lost if the memory is forgotten later.
            //
            // Since the memories might be maintaining state related to the passive effect, we're going to upgrade the
            // existing memory to act as the real new clarity, and downgrade the clarity to act as a replacement memory.
            //
            // There's nothing technically stopping remember + forget from also sharing state like this, but it doesn't
            // happen in practice so we ignore that issue here.
            currentMemory.isClarified = true;
            currentMemory.updateDescription();
            newClarity.isClarified = false;
            newClarity.updateDescription();

            AbstractMemory tmp = newClarity;
            newClarity = currentMemory;
            currentMemory = tmp;
        }

        if (newClarity.memoryType == MemoryType.SIN) {
            sinClarities.add(newClarity);
            sinClarities.sort(Comparator.comparing(m -> m.ID));
        } else {
            virtueClarities.add(newClarity);
            virtueClarities.sort(Comparator.comparing(m -> m.ID));
        }

        if (!passiveAlreadyActive) {
            newClarity.gainPassiveEffect();
        }

        AbstractRelic possibleMindglassRelic = this.owner.getRelic(MindglassRelic.ID);
        if (possibleMindglassRelic != null) {
            possibleMindglassRelic.onTrigger();
        }

        newClarity.flash();
        notifyModifyMemories();
    }

    public boolean hasClarity(String id) {
        return getClarity(id) != null;
    }

    public boolean isRemembering(String id) {
        return currentMemory != null && currentMemory.ID.equals(id);
    }

    public boolean hasMemoryOrClarity(String id) {
        return isRemembering(id) || hasClarity(id);
    }

    public AbstractMemory getClarity(String id) {
        for (AbstractMemory c : sinClarities) {
            if (c.ID.equals(id)) { return c; }
        }
        for (AbstractMemory c : virtueClarities) {
            if (c.ID.equals(id)) { return c; }
        }
        return null;
    }

    public void clear() {
        this.currentMemory = null;
        this.sinClarities.clear();
        this.virtueClarities.clear();
    }

    public void snap() {
        forgetCurrentMemoryNoNotify();

        for (AbstractMemory clarity : this.sinClarities) {
            clarity.losePassiveEffect();
        }
        this.sinClarities.clear();
        for (AbstractMemory clarity : this.virtueClarities) {
            clarity.losePassiveEffect();
        }
        this.virtueClarities.clear();

        notifyModifyMemories();
    }


    public void flashSnap() {
        AbstractPower snapPower = owner.getPower(SnappedPower.POWER_ID);
        if (snapPower != null) {
            snapPower.flash();
        }
    }

    public boolean isSnapped() { 
        return owner.hasPower(SnappedPower.POWER_ID);
    }
    
    public void notifyModifyMemories() {
        for (AbstractRelic r : owner.relics) {
            if (r instanceof OnModifyMemoriesListener) {
                ((OnModifyMemoriesListener)r).onModifyMemories();
            }
        }
        for (AbstractPower p : owner.powers) {
            if (p instanceof OnModifyMemoriesListener) {
                ((OnModifyMemoriesListener)p).onModifyMemories();
            }
        }
        AbstractDungeon.onModifyPower();
    }

    public int countCurrentClarities() {
        return sinClarities.size() + virtueClarities.size();
    }

    public ArrayList<AbstractMemory> currentClarities() {
        ArrayList<AbstractMemory> retVal = new ArrayList<>();
        retVal.addAll(sinClarities);
        retVal.addAll(virtueClarities);
        return retVal;
    }

    public void update() {
        currentMemoryHb.update();
        sinClarityStackHb.update();
        virtueClarityStackHb.update();
    }

    public void render(SpriteBatch sb) {
        if (currentMemory != null) {
            renderCurrentMemory(sb);
        }

        renderClarityStack(sb, sinClarities, sinClarityStackHb);
        renderClarityStack(sb, virtueClarities, virtueClarityStackHb);
    }

    private void renderCurrentMemory(SpriteBatch sb) {
        RenderUtils.renderAtlasRegionCenteredAt(sb, MEMORY_BACKGROUND_REGION32, drawX, drawY + currentMemoryOffsetY, MEMORY_BACKGROUND_COLOR);

        // the position args are for the center point
        currentMemory.render(sb, this.drawX, this.drawY + currentMemoryOffsetY, RenderUtils.UNMODIFIED_COLOR);

        ArrayList<AbstractMemory> memories = new ArrayList<>();
        memories.add(currentMemory);
        renderHitboxTips(sb, memories, currentMemoryHb);
    }

    private void renderClarityStack(SpriteBatch sb, ArrayList<AbstractMemory> clarities, Hitbox hb) {
        float yOffset = 0;
        for (AbstractMemory clarity : clarities) {
            yOffset += CLARITY_PADDING_Y;

            // the position args are for the center point
            clarity.render(sb, hb.cX, hb.y + yOffset, RenderUtils.UNMODIFIED_COLOR);
        }

        renderHitboxTips(sb, clarities, hb);
    }

    private void renderHitboxTips(SpriteBatch sb, ArrayList<AbstractMemory> memories, Hitbox hb) {
        if (hb.hovered && !memories.isEmpty()) {
            ArrayList<PowerTip> tips = new ArrayList<>();
            for (AbstractMemory c : memories) {
                tips.add(new PowerTip(c.name, c.description, c.region48));
            }

            // Based on the AbstractCreature.renderPowerTips impl
            float tipX = hb.cX + hb.width / 2.0F < TIP_X_THRESHOLD ?
                    hb.cX + hb.width / 2.0F + TIP_OFFSET_R_X :
                    hb.cX - hb.width / 2.0F + TIP_OFFSET_L_X;

            // The calculatedAdditionalOffset ensures everything is shifted to avoid going offscreen
            float tipY = hb.cY + TipHelper.calculateAdditionalOffset(tips, hb.cY);

            TipHelper.queuePowerTips(hb.cX, tipY, tips);
        }
    }

    public ArrayList<AbstractMemory> currentMemories() {
        ArrayList<AbstractMemory> retVal = currentClarities();
        if (currentMemory != null) {
            retVal.add(currentMemory);
        }
        return retVal;
    }

    public void movePosition(float x, float y) {
        this.drawX = x;
        this.drawY = y;
    }

    enum MemoryEventType {
        REMEMBER, CLARITY, FORGET, SNAP
    }
}
