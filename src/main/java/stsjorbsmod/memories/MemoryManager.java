package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.SnappedPower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryManager {
    private final Wanderer owner;

    public AbstractMemory currentMemory;
    private ArrayList<AbstractMemory> memories;

    public MemoryManager(Wanderer owner) {
        this.owner = owner;
        this.currentMemory = null;
        this.memories = MemoryUtils.allPossibleMemories(owner);
    }

    public static MemoryManager forPlayer() {
        return forPlayer(AbstractDungeon.player);
    }

    public static MemoryManager forPlayer(AbstractCreature target) {
        if (target instanceof Wanderer) {
            return ((Wanderer)target).memories;
        }
        return null;
    }

    public void rememberMemory(String id) {
        if (isSnapped()) {
            flashSnap();
        } else if (currentMemory != null && currentMemory.ID.equals(id)) {
            currentMemory.flashWithoutSound();
        } else {
            forgetCurrentMemoryNoNotify();

            this.currentMemory = getMemory(id);
            this.currentMemory.isRemembered = true;
            this.currentMemory.onRemember();
            this.currentMemory.updateDescription();
            if (!this.currentMemory.isClarified) {
                this.currentMemory.onGainPassiveEffect();
            }

            this.currentMemory.flash();
            notifyModifyMemories(MemoryEventType.REMEMBER);
        }
    }

    public void forgetCurrentMemory() {
        forgetCurrentMemoryNoNotify();
        notifyModifyMemories(MemoryEventType.FORGET);
    }

    private void forgetCurrentMemoryNoNotify() {
        if (this.currentMemory != null) {
            this.currentMemory.isRemembered = false;
            if (!this.currentMemory.isClarified) {
                this.currentMemory.onLosePassiveEffect();
            }
            this.currentMemory.onForget();
            this.currentMemory.updateDescription();
            this.currentMemory = null;
        }
    }

    public void gainClarityOfCurrentMemory() {
        if (currentMemory != null) {
            gainClarity(currentMemory.ID);
        }
    }

    public void gainClarity(String id) {
        if (isSnapped()) {
            flashSnap();
            return;
        }

        AbstractMemory clarity = getMemory(id);
        if (clarity.isClarified) {
            clarity.flashWithoutSound();
            return;
        }

        clarity.isClarified = true;
        if (!clarity.isRemembered) {
            clarity.onGainPassiveEffect();
        }
        clarity.updateDescription();
        clarity.flash();
        notifyModifyMemories(MemoryEventType.CLARITY);
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

    public AbstractMemory getMemory(String id) {
        for (AbstractMemory m : memories) {
            if (m.ID.equals(id)) { return m; }
        }
        return null;
    }

    public AbstractMemory getClarity(String id) {
        for (AbstractMemory m : memories) {
            if (m.ID.equals(id) && m.isClarified) { return m; }
        }
        return null;
    }

    public void clear() {
        this.currentMemory = null;
        this.memories.clear();
        this.memories = MemoryUtils.allPossibleMemories(owner);
    }

    public void snap() {
        if (isSnapped()) { return; }

        forgetCurrentMemoryNoNotify();

        for (AbstractMemory clarity : this.currentClarities()) {
            clarity.isClarified = false;
            clarity.onLosePassiveEffect();
        }

        notifyModifyMemories(MemoryEventType.SNAP);
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

    private void notifyPossibleModifyMemoryListener(Object possibleListener, MemoryEventType type) {
        if (possibleListener instanceof OnModifyMemoriesListener) {
            OnModifyMemoriesListener listener = (OnModifyMemoriesListener) possibleListener;
            if (Arrays.asList(listener.getMemoryEventTypes()).contains(type)) {
                listener.onModifyMemories();
            }
        }
    }

    public void notifyModifyMemories(MemoryEventType type) {
        owner.relics.forEach(possibleListener -> notifyPossibleModifyMemoryListener(possibleListener, type));
        owner.powers.forEach(possibleListener -> notifyPossibleModifyMemoryListener(possibleListener, type));
        this.memories.forEach(possibleListener -> notifyPossibleModifyMemoryListener(possibleListener, type));

        AbstractDungeon.onModifyPower();
    }

    public int countCurrentClarities() {
        return (int) memories.stream().filter(m -> m.isClarified).count();
    }

    public List<AbstractMemory> currentClarities() {
        return memories.stream().filter(m -> m.isClarified).collect(Collectors.toList());
    }

    public List<AbstractMemory> currentMemories() {
        return memories.stream().filter(m -> m.isClarified || m.isRemembered).collect(Collectors.toList());
    }

    private static final float MEMORY_ARC_X_RADIUS = 185F * Settings.scale;
    private static final float MEMORY_ARC_Y_RADIUS = 195F * Settings.scale;
    private static final float MEMORY_ARC_ANGLE = 230F;
    private static final float MEMORY_ARC_Y_OFFSET = 140F * Settings.scale;

    public void update(float centerX, float centerY) {
        for (int i = 0; i < memories.size(); ++i) {
            float relativeMemoryAngle = MEMORY_ARC_ANGLE * (((float)i) / (memories.size() - 1));
            float absoluteArcStartAngle = 90.0F - MEMORY_ARC_ANGLE / 2.0F;
            float absoluteAngle = absoluteArcStartAngle + relativeMemoryAngle;
            float x = MEMORY_ARC_X_RADIUS * MathUtils.cosDeg(absoluteAngle) + centerX;
            float y = MEMORY_ARC_Y_RADIUS * MathUtils.sinDeg(absoluteAngle) + centerY + MEMORY_ARC_Y_OFFSET;

            memories.get(i).update(x, y);
        }
    }

    public void render(SpriteBatch sb) {
        for (AbstractMemory m : memories) {
            m.render(sb);
        }
    }

    public static final MemoryEventType[] ALL_MEMORY_EVENTS = MemoryEventType.values();
    public enum MemoryEventType {
        REMEMBER, CLARITY, FORGET, SNAP
    }
}
