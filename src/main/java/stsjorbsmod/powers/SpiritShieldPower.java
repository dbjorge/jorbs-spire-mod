package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class SpiritShieldPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(SpiritShieldPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("spirit_shield_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("spirit_shield_power32.png"));

    private static final int DAMAGE_REDUCTION = 1;

    public SpiritShieldPower(final AbstractCreature owner, final int numberTurns) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;
        this.amount = numberTurns;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            this.flash();
            return damageAmount - DAMAGE_REDUCTION;
        } else {
            return damageAmount;
        }
    }

    @Override
    public void updateDescription() {
        description = this.amount <= 1 ?
                DESCRIPTIONS[0] + DAMAGE_REDUCTION + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] :
                (DESCRIPTIONS[0] + DAMAGE_REDUCTION + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[3]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpiritShieldPower(owner, amount);
    }
}