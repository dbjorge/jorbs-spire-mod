package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

// TODO: It would be nice for this to update if player gold changes mid-combat
public class CharityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(CharityMemory.class);

    private static final int STRENGTH_PER_GOLD_THRESHOLD = 1;
    private static final int GOLD_THRESHOLD = 100;

    public CharityMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
        this.descriptionPlaceholders.put("!S!", STRENGTH_PER_GOLD_THRESHOLD+"");
        this.descriptionPlaceholders.put("!G!", GOLD_THRESHOLD+"");
    }

    private int strengthAdded;

    private int calculateStrengthToAdd() {
        return (AbstractDungeon.player.gold / GOLD_THRESHOLD) * STRENGTH_PER_GOLD_THRESHOLD;
    }

    @Override
    public void onRemember() {
        this.strengthAdded = calculateStrengthToAdd();
        if (this.strengthAdded > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, source, new StrengthPower(owner, this.strengthAdded), this.strengthAdded));
        }
    }

    @Override
    public void onForget() {
        if (this.strengthAdded > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(owner, source, StrengthPower.POWER_ID, this.strengthAdded));
        }
    }
}
