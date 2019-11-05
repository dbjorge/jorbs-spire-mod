package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;

public class ChainLightningAction extends AbstractGameAction {
    private AbstractCreature owner;
    private AbstractMonster initialTarget;
    ArrayList<AbstractMonster> allTargets;
    private int damage;
    private int extraDamagePerHop;

    public ChainLightningAction(AbstractCreature owner, AbstractMonster initialTarget, ArrayList<AbstractMonster> allTargets, int damage, int extraDamagePerHop, AttackEffect attackEffect) {
        this.owner = owner;
        this.initialTarget = initialTarget;
        this.allTargets = allTargets;
        this.damage = damage;
        this.extraDamagePerHop = extraDamagePerHop;
        this.attackEffect = attackEffect;
    }

    public void update() {
        ArrayList<AbstractMonster> remainingTargets = new ArrayList<>(allTargets);
        int nextTargetIndex = remainingTargets.indexOf(initialTarget);
        int currentDamage = this.damage;
        int multiplier = 0;

        while(true) {
            AbstractMonster nextTarget = remainingTargets.remove(nextTargetIndex);

            if (!nextTarget.halfDead && !nextTarget.isDying && !nextTarget.isEscaping) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(nextTarget, new DamageInfo(owner, currentDamage, DamageInfo.DamageType.NORMAL), this.attackEffect));
                addLightningEffect(nextTarget, multiplier);
                multiplier += 1;
                currentDamage += this.extraDamagePerHop;
            }

            if (remainingTargets.isEmpty()) {
                break;
            }

            nextTargetIndex = AbstractDungeon.cardRandomRng.random(0, remainingTargets.size() - 1);
        }

        this.isDone = true;
    }

    private void addLightningEffect(AbstractMonster monster, int multiplier) {
        float duration = (0.05F * multiplier);
        AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", duration));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(monster.drawX, monster.drawY), duration));
    }
}
