package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.BurningLoseHpAction;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class BurningPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {
    public static final String POWER_ID = JorbsMod.makeID(BurningPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("burning_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("burning_power32.png"));

    private AbstractCreature source;
    private boolean justApplied = false;
    public boolean generatedByPyromancy;

    public BurningPower(AbstractCreature owner, AbstractCreature source, int burningAmt) {
        this(owner, source, burningAmt, false);
    }

    public BurningPower(AbstractCreature owner, AbstractCreature source, int burningAmt, boolean generatedByPyromancy) {
        this.name = NAME;
        this.ID = POWER_ID;
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

        this.updateDescription();
        this.loadRegion("attackBurn"); // TODO use community art
        // this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        // this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.05F);
    }

    @Override
    public void updateDescription() {
        if (amount <= 0) {
            this.description = DESCRIPTIONS[4];
        } else if (this.owner != null && !this.owner.isPlayer) {
            this.description = DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[1] + (this.amount - this.amount / 2) + DESCRIPTIONS[2] + DESCRIPTIONS[4];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + (this.amount - this.amount / 2) + DESCRIPTIONS[2] + DESCRIPTIONS[4];
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
        return healAmount / 2;
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
