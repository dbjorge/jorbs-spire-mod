package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.actions.BurningLoseHpAction;
import stsjorbsmod.characters.Wanderer;

/**
 * Any and all references to poison are either accidental or temporary as we don't have the effect yet.
 */
public class BurningPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {
    public static final String POWER_ID = "Burning";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private AbstractCreature source;

    public BurningPower(AbstractCreature owner, AbstractCreature source, int burningAmt) {
        this.name = NAME;
        this.ID = "Burning";
        this.owner = owner;
        this.source = source;
        this.amount = burningAmt;
        if (this.amount >= 9999) {
            this.amount = 9999;
        }

        this.updateDescription();
        this.loadRegion("burning");
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    public void updateDescription() {
        if (this.owner != null && !this.owner.isPlayer) {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }
    }

    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
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
    public void on(){

    }

    @Override
    public AbstractPower makeCopy() {
        return new BurningPower(this.owner, this.source, this.amount);
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }
}
