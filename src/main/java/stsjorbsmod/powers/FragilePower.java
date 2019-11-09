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
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class FragilePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(FragilePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("fragile_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("fragile_power32.png"));

    public FragilePower(final AbstractCreature owner, final int turnsUntilSnap) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;
        this.amount = turnsUntilSnap;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 1) {
            AbstractDungeon.actionManager.addToBottom(new SnapAction(owner));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }

    @Override
    public void updateDescription() {
        description = this.amount <= 1 ?
                DESCRIPTIONS[0] :
                (DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FragilePower(owner, amount);
    }
}