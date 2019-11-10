package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryUtils;

public class GainClarityOfCurrentMemoryAction extends AbstractGameAction {
    private String specificMemoryID;

    public GainClarityOfCurrentMemoryAction(AbstractCreature owner) {
        this.setValues(owner, owner);
    }

    public GainClarityOfCurrentMemoryAction(AbstractCreature owner, String onlyIfRememberingThisMemoryID) {
        this.setValues(owner, owner);
        this.specificMemoryID = onlyIfRememberingThisMemoryID;
    }

    public void update() {
        MemoryManager m = MemoryManager.forPlayer(target);
        if (specificMemoryID == null || (m.currentMemory != null && m.currentMemory.ID == specificMemoryID)) {
            MemoryManager.forPlayer(target).gainClarityOfCurrentMemory();
        }

        isDone = true;
    }
}
