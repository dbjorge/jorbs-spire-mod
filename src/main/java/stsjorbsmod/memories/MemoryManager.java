package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import stsjorbsmod.characters.Wanderer;

import java.util.ArrayList;

// For now, this exists side-by-side with the memories/clarities also being tracked like Powers, and is primarily
// handling rendering the icons on the sides/top rather than the power bar.
//
// The intention is for this class to eventually *replace* AbstractCreature.powers as the source of truth, and for
// memories/clarities to not live in the powers list at all. We'll need to hook up the necessary power on* hooks to
// also go through here before we can make that change, though.
public class MemoryManager {
    private static final float MEMORY_HB_WIDTH = 64F * Settings.scale;
    private static final float MEMORY_HB_HEIGHT = 64F * Settings.scale;
    private static final float CLARITY_PADDING_Y = 32.0F * Settings.scale;
    private static final float MAX_CLARITIES_PER_STACK = 7;
    private static final float CLARITY_HB_WIDTH = 64F * Settings.scale;
    private static final float CLARITY_HB_HEIGHT = CLARITY_PADDING_Y * MAX_CLARITIES_PER_STACK;
    private static final Color ICON_COLOR = new Color(1.0F, 1.0F, 1.0F, 1.0F);
    private static final float TIP_X_THRESHOLD = 1544.0F * Settings.scale;
    private static final float TIP_OFFSET_R_X = 20.0F * Settings.scale;
    private static final float TIP_OFFSET_L_X = -380.0F * Settings.scale;

    private final float drawX;
    private final float drawY;
    private final float currentMemoryOffsetY;
    private final Hitbox sinClarityStackHb;
    private final Hitbox virtueClarityStackHb;
    private final Hitbox currentMemoryHb;

    private AbstractMemory currentMemory;

    // We maintain these separately because rendering needs them separated and that's the hot path.
    private final ArrayList<AbstractMemory> sinClarities = new ArrayList<>();
    private final ArrayList<AbstractMemory> virtueClarities = new ArrayList<>();

    public MemoryManager(float drawX, float drawY, float currentMemoryOffsetY, float clarityOffsetY, float sinOffsetX, float virtueOffsetX) {
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
        if (memoryToRemember.isClarified) {
            addClarity(memoryToRemember);
        } else {
            this.currentMemory = memoryToRemember;
        }
    }

    public void gainClarityOfCurrentMemory() {
        addClarity(currentMemory);
        currentMemory = null;
    }

    public void snap() {
        this.currentMemory = null;
        this.sinClarities.clear();
        this.virtueClarities.clear();
    }

    public void addClarity(AbstractMemory m) {
        if (m.memoryType == MemoryType.SIN) {
            sinClarities.add(m);
        } else {
            virtueClarities.add(m);
        }
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
        // the position args are for the center point
        currentMemory.renderIcons(sb, this.drawX, this.drawY + currentMemoryOffsetY, ICON_COLOR);

        ArrayList<AbstractMemory> memories = new ArrayList<>();
        memories.add(currentMemory);
        renderHitboxTips(sb, memories, currentMemoryHb);
    }

    private void renderClarityStack(SpriteBatch sb, ArrayList<AbstractMemory> clarities, Hitbox hb) {
        float yOffset = 0;
        for (AbstractMemory clarity : clarities) {
            yOffset += CLARITY_PADDING_Y;

            // the position args are for the center point
            clarity.renderIcons(sb, hb.cX, hb.y + yOffset, ICON_COLOR);
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
}
