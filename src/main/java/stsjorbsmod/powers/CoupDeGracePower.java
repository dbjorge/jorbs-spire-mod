package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.patches.TrueDamagePatch;

public class CoupDeGracePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(CoupDeGracePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static int outputDamage = 0;

    public CoupDeGracePower(final AbstractCreature owner, final int turnsUntilDamage) {
        super(STATIC);

        this.owner = owner;
        this.isTurnBased = true;
        this.amount = turnsUntilDamage;
        this.type = PowerType.DEBUFF;
        updateDescription();
    }

    public static void increaseBaseOutputDamage(int incomingDamage, int currentBlock) {
        if (incomingDamage > currentBlock)
            outputDamage += (incomingDamage - currentBlock - 1) * 2;
    }

    @Override
    public void atEndOfRound() {
        this.amount--;
        if (this.amount == 0) {
            DamageInfo damageInfo = new DamageInfo(AbstractDungeon.player, outputDamage, DamageInfo.DamageType.THORNS);
            TrueDamagePatch.TrueDamageInfoField.isTrueDamage.set(damageInfo, true);
            addToBot(new DamageAction(this.owner, damageInfo, AbstractGameAction.AttackEffect.SMASH));

            outputDamage = 0;

            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        updateDescription();

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = (amount == 1 ?
                String.format(DESCRIPTIONS[(owner instanceof AbstractPlayer ? 2 : 0) + 0], amount, outputDamage)
                : String.format(DESCRIPTIONS[(owner instanceof AbstractPlayer ? 2 : 0) + 1], amount, outputDamage));
    }

    @Override
    public AbstractPower makeCopy() {
        return new CoupDeGracePower(owner, amount);
    }
}
