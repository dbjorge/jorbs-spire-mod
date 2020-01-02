package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

// attacks with random targets (specifically, uses of the AttackDamageRandomEnemyAction) target this enemy.
// This is primarily a marker power for applyPossibleActionTargetOverride to look for.
public class BlackTentaclesPower extends CustomJorbsModPower implements AtEndOfPlayerTurnSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(BlackTentaclesPower.class);
    public static final String POWER_ID = STATIC.ID;

    public AbstractCreature source;

    public BlackTentaclesPower(final AbstractCreature owner, final AbstractCreature source) {
        super(STATIC);

        this.type = PowerType.DEBUFF;

        this.owner = owner;
        this.source = source;
        this.amount = -1; // non-stackable

        this.isTurnBased = true;
    }

    @Override
    public void onInitialApplication() {
        // only 1 target can have this power at a time; subsequent uses of the card will overwrite the old effect
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            for (AbstractPower p : m.powers) {
                if (p.ID.equals(this.ID) && p != this) {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(m, source, p));
                }
            }
        }
    }

    @Override
    public void atEndOfPlayerTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlackTentaclesPower(owner, source);
    }

    public int onAnyMonsterHpLoss(AbstractMonster monster, DamageInfo originalDamageInfo, int originalHpLoss) {
        // We accept null owner primarily for the sake of ExplosivePotion, see #299.
        boolean isDamageFromApplicableSource = originalDamageInfo != null && (originalDamageInfo.owner == null || originalDamageInfo.owner == this.source);

        if (monster != owner && isDamageFromApplicableSource && originalHpLoss > 0) {
            this.flash();
            DamageType newDamageType = originalDamageInfo.type == DamageType.HP_LOSS ? DamageType.HP_LOSS : DamageType.THORNS;
            DamageInfo newDamageInfo = new DamageInfo(source, originalHpLoss, newDamageType);
            AbstractDungeon.actionManager.addToTop(new DamageAction(owner, newDamageInfo, AttackEffect.SLASH_DIAGONAL));
            return 0;
        }
        return originalHpLoss;
    }
}

