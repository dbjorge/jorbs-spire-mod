package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;

public class CoilPower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(CoilPower.class);
    public static final String POWER_ID = STATIC.ID;

    public static final int DAMAGE_PER_COIL = 1;

    public CoilPower(final AbstractCreature owner, final int amount) {
        super(STATIC);

        this.owner = owner;
        this.amount = amount;

        updateDescription();
    }

    private int calculateDamage() {
        return amount * DAMAGE_PER_COIL;
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], calculateDamage());
    }

    private void consumeCoilForDamage() {
        this.flash();

        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(calculateDamage(), true), DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HORIZONTAL));
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(owner, owner, CoilPower.POWER_ID));
        }
    }

    @Override
    public void onRememberMemory(String id) {
        consumeCoilForDamage();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (owner.hasPower(SnappedPower.POWER_ID)) {
            consumeCoilForDamage();
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CoilPower(owner, amount);
    }
}
