package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class FocusOnThePainPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = JorbsMod.makeID(FocusOnThePainPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("focus_on_the_pain_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("focus_on_the_pain_power32.png"));

    public FocusOnThePainPower(final AbstractCreature owner) {
        ID = POWER_ID;
        this.name = NAME;

        this.owner = owner;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override public void onAttack(DamageInfo damageInfo, int damage, AbstractCreature target) { }

    @Override
    public int onLoseHp(int damageAmount) {
        AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(owner));
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FocusOnThePainPower(owner);
    }
}
