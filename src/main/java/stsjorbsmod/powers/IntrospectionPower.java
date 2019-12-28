package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

public class IntrospectionPower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(IntrospectionPower.class);
    public static final String POWER_ID = STATIC.ID;

    public int baseDamage;
    public int damagePerClarity;

    public IntrospectionPower(final AbstractCreature owner, final int loseHpAmount, final int baseDamage, final int damagePerClarity) {
        super(STATIC);

        this.owner = owner;

        this.amount2 = loseHpAmount;
        this.baseDamage = baseDamage;
        this.damagePerClarity = damagePerClarity;

        recalculate();
    }

    private void recalculate() {
        amount = baseDamage + damagePerClarity * MemoryManager.forPlayer(owner).countCurrentClarities();
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, amount2));
            int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
            AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(owner, damageMatrix, DamageType.THORNS, AttackEffect.FIRE));
        }
    }

    @Override
    public void onGainClarity(String id) {
        recalculate();
    }

    @Override
    public void onLoseClarity(String id) {
        recalculate();
    }

    @Override
    public void onSnap() {
        recalculate();
    }

    @Override
    public void stackPower(AbstractPower otherIntrospectionPower) {
        IntrospectionPower other = (IntrospectionPower) otherIntrospectionPower;
        this.amount2 += other.amount2;
        this.baseDamage += other.baseDamage;
        this.damagePerClarity += other.damagePerClarity;

        recalculate();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.damagePerClarity + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new IntrospectionPower(owner, amount2, baseDamage, damagePerClarity);
    }
}

