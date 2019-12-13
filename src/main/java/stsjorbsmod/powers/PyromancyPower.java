package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class PyromancyPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(PyromancyPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("pyromancy_power32.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("pyromancy_power84.png"));

    public PyromancyPower(final AbstractCreature owner, final int numExtraTimesToBurn) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;
        this.amount = numExtraTimesToBurn;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (source == owner && power.ID.equals(BurningPower.POWER_ID) && !((BurningPower)power).generatedByPyromancy) {
            for (int i = 0; i < amount; ++i) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new BurningPower(target, source, power.amount, true)));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new PyromancyPower(owner, amount);
    }
}

