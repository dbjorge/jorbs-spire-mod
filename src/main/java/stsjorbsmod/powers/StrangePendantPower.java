package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class StrangePendantPower extends AbstractPower implements OnPlayerHpLossPowerSubscriber {
    public static final String POWER_ID = JorbsMod.makeID(StrangePendantPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("strange_pendant_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("strange_pendant_power32.png"));

    private int damageAbsorbedThisTurn = 0;

    public StrangePendantPower(AbstractCreature owner, int damageReduction) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = damageReduction;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
    }

    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.damageAbsorbedThisTurn = 0;
    }

    @Override
    public int onPlayerHpLoss(int originalHpLoss) {
        int remainingAbsorb = amount - damageAbsorbedThisTurn;
        if (remainingAbsorb <= 0 || originalHpLoss <= 0) { return originalHpLoss; }

        this.flash();
        int damageToAbsorb = Math.min(originalHpLoss, remainingAbsorb);
        damageAbsorbedThisTurn += damageToAbsorb;
        return originalHpLoss - damageToAbsorb;
    }
}
