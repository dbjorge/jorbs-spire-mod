package stsjorbsmod.memories;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.CoilPower;
import stsjorbsmod.powers.SnappedPower;

import java.util.ArrayList;
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
            this.currentMemory.onRemember();
            this.currentMemory.isRemembered = true;
            this.currentMemory.updateDescription();
            if (!this.currentMemory.isClarified) {
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
            if (!this.currentMemory.isClarified) {
                this.currentMemory.losePassiveEffect();
            }
            this.currentMemory.onForget();
            this.currentMemory.isRemembered = false;
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

        if (!clarity.isPassiveEffectActive) {
            clarity.gainPassiveEffect();
        }
        clarity.isClarified = true;
        clarity.updateDescription();
        clarity.flash();
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
        forgetCurrentMemoryNoNotify();

        for (AbstractMemory clarity : this.currentClarities()) {
            clarity.losePassiveEffect();
            clarity.isClarified = false;
        }

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
        for (AbstractPower p : owner.powers) {
            if (p instanceof OnModifyMemoriesListener) {
                ((OnModifyMemoriesListener)p).onModifyMemories();
            }
        }
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

    private static final float MEMORY_ARC_RADIUS = 180F;

    public void update(float centerX, float centerY) {
        for (int i = 0; i < memories.size(); ++i) {
            float dist = MEMORY_ARC_RADIUS * Settings.scale;

            // This is mostly taken from orb rendering code
            // TODO clean this up and give the constants names
            float angle = 100.0F + (float)14 * 12.0F;
            float offsetAngle = angle / 2.0F;
            angle *= (float)i / ((float)14 - 1.0F);
            angle += 90.0F - offsetAngle;
            float x = dist * MathUtils.cosDeg(angle) + AbstractDungeon.player.drawX;
            float y = dist * MathUtils.sinDeg(angle) + AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F;

            memories.get(i).update(x, y);
        }
    }

    public void render(SpriteBatch sb) {
        for (AbstractMemory m : memories) {
            m.render(sb);
        }
    }
}
