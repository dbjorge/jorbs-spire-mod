package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;

public class KindnessMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(KindnessMemory.class);

    private static final int ENEMY_STRENGTH_REDUCTION = 3;

    private ArrayList<AbstractGameAction> restoreStrengthActions;

    public KindnessMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
        setDescriptionPlaceholder("!M!", ENEMY_STRENGTH_REDUCTION);
    }

    @Override
    public void onGainPassiveEffect() {
        this.restoreStrengthActions = new ArrayList<>();

        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            // add to top for the purposes of hold monster ordering.
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(mo, owner, new StrengthPower(mo, -ENEMY_STRENGTH_REDUCTION), -ENEMY_STRENGTH_REDUCTION, true, AbstractGameAction.AttackEffect.NONE));

            if (!mo.hasPower(ArtifactPower.POWER_ID)) {
                this.restoreStrengthActions.add(
                        new ApplyPowerAction(mo, owner, new StrengthPower(mo, +ENEMY_STRENGTH_REDUCTION), +ENEMY_STRENGTH_REDUCTION, true, AbstractGameAction.AttackEffect.NONE));
            }
        }
    }

    @Override
    public void onLosePassiveEffect() {
        for (AbstractGameAction restoreAction : this.restoreStrengthActions) {
            AbstractDungeon.actionManager.addToBottom(restoreAction);
        }
        restoreStrengthActions.clear();
    }
}
