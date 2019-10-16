package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.powers.SnappedPower;
import stsjorbsmod.memories.MemoryUtils;


// At the end of turn 7 deal 5 damage to all enemies and 2 damage to yourself for every clarity bonus you have. You cannot be affected by memories or clarities for the remainder of the fight.
public class SnapAction extends AbstractGameAction {
    private final int ENEMY_DAMAGE_PER_CLARITY = 5;
    private final int PLAYER_DAMAGE_PER_CLARITY = 2;

    public SnapAction(AbstractCreature target) {
        this.target = target;
    }

    public void update() {
        if (target.hasPower(SnappedPower.POWER_ID)) {
            isDone = true;
            return;
        }

        int numClarities = MemoryUtils.countClarities(target);
        int enemyDamage = ENEMY_DAMAGE_PER_CLARITY * numClarities;
        int targetDamage = PLAYER_DAMAGE_PER_CLARITY * numClarities;

        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(target, (AbstractCreature)null, new SnappedPower(target)));
        AbstractDungeon.actionManager.addToTop(
                new DamageAction(target, new DamageInfo(target, targetDamage, DamageInfo.DamageType.THORNS), AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToTop(
                new DamageAllEnemiesAction((AbstractCreature)null, DamageInfo.createDamageMatrix(enemyDamage, true), DamageInfo.DamageType.THORNS, AttackEffect.BLUNT_LIGHT));

        isDone = true;
    }
}



