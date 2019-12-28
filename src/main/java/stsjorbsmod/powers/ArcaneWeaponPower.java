package stsjorbsmod.powers;

import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.ArcaneWeaponAction;

// Attack a random enemy for !D! damage at the end of each turn. (affected by str, vuln, etc)
public class ArcaneWeaponPower extends CustomJorbsModPower implements OnPowersModifiedSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ArcaneWeaponPower.class);
    public static final String POWER_ID = STATIC.ID;

    private AbstractCard backingCard;

    private static long instanceCounter = 0;

    public ArcaneWeaponPower(final AbstractCreature owner, final AbstractCard backingCard) {
        super(STATIC);

        // This prevents the power from stacking with other instances of itself for different card instances.
        // This is the same strategy used by TheBombPower.
        //
        // StSLib provides a NonStackablePower interface with similar functionality, but we're intentionally not using
        // it because it is hackier than the ID thing.
        ID = POWER_ID + "__" + (++instanceCounter);

        this.type = PowerType.BUFF;
        this.owner = owner;
        this.backingCard = backingCard.makeStatEquivalentCopy();

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

