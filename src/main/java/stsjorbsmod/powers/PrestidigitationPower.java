package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class PrestidigitationPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCard sourceCard;

    public static final String POWER_ID = JorbsMod.makeID(PrestidigitationPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("prestidigitation_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("prestidigitation_power32.png"));

    public PrestidigitationPower(final AbstractCreature owner, final int amount) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer == owner.isPlayer) {
            AbstractCreature target = AbstractDungeon.getRandomMonster();
            if (target != null) {
                this.flash();
                AbstractPower effect = AbstractDungeon.cardRandomRng.randomBoolean() ?
                        new WeakPower(target, amount, !owner.isPlayer) :
                        new VulnerablePower(target, amount, !owner.isPlayer);

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, owner, effect, 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + (this.amount == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new PrestidigitationPower(owner, amount);
    }
}

