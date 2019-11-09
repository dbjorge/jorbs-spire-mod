package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.MemoryUtils;
import stsjorbsmod.util.TextureLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class MagicMirrorPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(MagicMirrorPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("magic_mirror_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("magic_mirror_power32.png"));

    public MagicMirrorPower(final AbstractCreature owner, final int amount) {
        ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    private AbstractPower tryCreateReflectedPower(AbstractCreature newTarget, AbstractCreature originalTarget, AbstractPower originalPower) {
        try {
            Class<? extends AbstractPower> originalClass = originalPower.getClass();
            try {
                return originalClass.getConstructor(AbstractCreature.class, int.class, boolean.class)
                        .newInstance(newTarget, originalPower.amount, owner.isPlayer);
            } catch (NoSuchMethodException e) {}
            try {
                return originalClass.getConstructor(AbstractCreature.class, int.class)
                        .newInstance(newTarget, originalPower.amount);
            } catch (NoSuchMethodException e) {}
            try {
                return originalClass.getConstructor(AbstractCreature.class)
                        .newInstance(newTarget);
            } catch (NoSuchMethodException e) {}
        } catch (Exception e) {
            JorbsMod.logger.error("Unable to createReflectedPower of " + originalPower, e);
        }
        return null;
    }

    public void onPowerReceived(AbstractPower originalPower) {
        if (originalPower.type == PowerType.DEBUFF) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    for (int i = 0; i < this.amount; ++i) {
                        AbstractPower reflectedPower = tryCreateReflectedPower(m, owner, originalPower);
                        if (reflectedPower != null) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, owner, reflectedPower));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }

    }

    @Override
    public AbstractPower makeCopy() {
        return new MagicMirrorPower(owner, amount);
    }
}

