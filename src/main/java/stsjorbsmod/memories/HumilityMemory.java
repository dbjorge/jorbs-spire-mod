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
import com.megacrit.cardcrawl.powers.ThornsPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class HumilityMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(HumilityMemory.class);

    private static final int THORNS_DAMAGE = 2;

    public HumilityMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        setDescriptionPlaceholder("!M!", THORNS_DAMAGE);
    }

    @Override
    public void onGainPassiveEffect() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new ThornsPower(owner, THORNS_DAMAGE)));
    }

    @Override
    public void onLosePassiveEffect() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, owner, ThornsPower.POWER_ID, THORNS_DAMAGE));
    }
}
