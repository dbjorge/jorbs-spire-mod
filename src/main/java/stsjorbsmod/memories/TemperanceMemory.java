package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class TemperanceMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(TemperanceMemory.class);

    private static final int ENEMY_STRENGTH_REDUCTION = 3;

    private ArrayList<AbstractGameAction> restoreStrengthActions;

    public TemperanceMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
    }

    @Override
    public void onRemember() {
        this.restoreStrengthActions = new ArrayList<>();

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(mo, owner, new StrengthPower(mo, -ENEMY_STRENGTH_REDUCTION), -ENEMY_STRENGTH_REDUCTION, true, AttackEffect.NONE));

            if (!mo.hasPower(ArtifactPower.POWER_ID)) {
                this.restoreStrengthActions.add(
                        new ApplyPowerAction(mo, owner, new StrengthPower(mo, +ENEMY_STRENGTH_REDUCTION), +ENEMY_STRENGTH_REDUCTION, true, AttackEffect.NONE));
            }
        }
    }

    @Override
    public void onForget() {
        for (AbstractGameAction restoreAction : this.restoreStrengthActions) {
            AbstractDungeon.actionManager.addToBottom(restoreAction);
        }
        restoreStrengthActions.clear();
    }

    @Override
    protected void updateMemoryDescription() {
        description = DESCRIPTIONS[0] + ENEMY_STRENGTH_REDUCTION + DESCRIPTIONS[1];
    }
}
