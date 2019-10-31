package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class LustMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(LustMemory.class);

    private static final float ATTACK_BONUS_MODIFIER = 1.25f;
    private static final int ATTACK_BONUS_PERCENTAGE_DESCRIPTION = 25;
    private static final int WEAK_ON_FORGET = 2;

    public LustMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!P!", ATTACK_BONUS_PERCENTAGE_DESCRIPTION);
        setDescriptionPlaceholder("!W!", WEAK_ON_FORGET);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (isPassiveEffectActive && type == DamageInfo.DamageType.NORMAL) {
            return damage * ATTACK_BONUS_MODIFIER;
        } else {
            return damage;
        }
    }

    @Override
    public void onForget() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new WeakPower(owner, WEAK_ON_FORGET, false)));
    }
}
