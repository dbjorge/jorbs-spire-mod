package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ArcaneWeaponAction;
import stsjorbsmod.effects.ArcaneWeaponEffect;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

// Attack a random enemy for !D! damage at the end of each turn. (affected by str, vuln, etc)
public class ArcaneWeaponPower extends AbstractPower implements CloneablePowerInterface, OnPowersModifiedSubscriber {
    public static final String POWER_ID = JorbsMod.makeID(ArcaneWeaponPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("diligence_memory_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("diligence_memory_power32.png"));

    private AbstractCard backingCard;

    private static long instanceCounter = 0;

    public ArcaneWeaponPower(final AbstractCreature owner, final AbstractCard backingCard) {
        // This prevents the power from stacking with other instances of itself for different card instances.
        // This is the same strategy used by TheBombPower.
        //
        // StSLib provides a NonStackablePower interface with similar functionality, but we're intentionally not using
        // it because it is hackier than the ID thing.
        ID = POWER_ID + "__" + (++instanceCounter);

        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.backingCard = backingCard.makeStatEquivalentCopy();

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            // Deferring the random selection to a separate action is important for preventing multiple Arcane Weapons
            // from picking the same enemy and then having some of them attack a corpse.
            AbstractDungeon.actionManager.addToBottom(new ArcaneWeaponAction(backingCard));
        }
    }

    @Override
    public void updateDescription() {
        backingCard.applyPowers();
        amount = backingCard.damage;
        description = String.format(DESCRIPTIONS[0], backingCard.isDamageModified ? "#g" : "#b", backingCard.damage);
    }

    @Override
    public AbstractPower makeCopy() {
        return new ArcaneWeaponPower(owner, backingCard.makeStatEquivalentCopy());
    }

    @Override
    public void receivePowersModified() {
        updateDescription();
    }
}

