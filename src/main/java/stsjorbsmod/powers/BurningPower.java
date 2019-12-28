package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.actions.BurningLoseHpAction;
import stsjorbsmod.util.BurningUtils;

public class BurningPower extends CustomJorbsModPower implements HealthBarRenderPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(BurningPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static final int HEAL_REDUCTION_PERCENTAGE = 100;

    private AbstractCreature source;
    private boolean justApplied = false;
    public boolean generatedByPyromancy;

    public BurningPower(AbstractCreature owner, AbstractCreature source, int burningAmt) {
        this(owner, source, burningAmt, false);
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int burningAmt, boolean generatedByPyromancy) {
        super(STATIC);

        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        this.owner = owner;
        this.source = source;
        this.amount = burningAmt;
        this.generatedByPyromancy = generatedByPyromancy;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }
        if (!source.isPlayer) {
            this.justApplied = true;
        }

        this.loadRegion("attackBurn"); // TODO remove this once we have a unique icon

        this.updateDescription();
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    @Override
    public void updateDescription() {
        if (this.amount <= 0) {
            // "Reduce healing by %1$s%."
            this.description = String.format(DESCRIPTIONS[2], HEAL_REDUCTION_PERCENTAGE);
        } else {
            int amountToReduceBy = amount - BurningUtils.calculateNextBurningAmount(this.source, this.amount);
            if (this.owner != null && !this.owner.isPlayer) {
                // "At the start of its turn, takes #b%1$s damage, then reduce #yBurning by #b%2$s. You cannot heal."
                this.description = String.format(DESCRIPTIONS[1], this.amount, amountToReduceBy);
            } else {
                // "At the start of your turn, take #b%1$s damage, then reduce #yBurning by #b%2$s. It cannot heal."
                this.description = String.format(DESCRIPTIONS[0], this.amount, amountToReduceBy);
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flashWithoutSound();
            AbstractDungeon.actionManager.addToBottom(
                    new BurningLoseHpAction(this.owner, this.source, this.amount, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }
    }

    @Override
    public int onHeal(int healAmount) {
        return 0;
    }

    @Override
    public void onRemove() {
        if (owner instanceof TimeEater) {
            // This should happen after burning gets removed, but before the healing action.
            AbstractDungeon.actionManager.actions.forEach(a -> updateHealing(a, owner));
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, DESCRIPTIONS[3], true));
        }
    }

    private static void updateHealing(AbstractGameAction action, AbstractCreature c) {
        if (action instanceof HealAction && action.target == c && action.source == c) {
            action.amount = 0;
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurningPower(this.owner, this.source, this.amount);
    }

    @Override
    public int getHealthBarAmount() {
        int amount = this.amount - owner.currentBlock;
        return Math.max(amount, 0);
    }

    @Override
    public Color getColor() {
        return Color.GOLD;
    }
}
