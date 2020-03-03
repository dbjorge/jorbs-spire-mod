package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.PrideMemory;
import stsjorbsmod.powers.FragilePower;
import stsjorbsmod.powers.SnappedPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;
import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class Determination extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Determination.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int TURNS_UNTIL_SNAP = 1;

    public Determination() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TURNS_UNTIL_SNAP;
        exhaust = true;

        tags.add(PERSISTENT_POSITIVE_EFFECT);
        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, PrideMemory.STATIC.ID));
        if (!upgraded) {
            if (p.hasPower(FragilePower.POWER_ID)) {
                FragilePower fragilePower = (FragilePower) p.getPower(FragilePower.POWER_ID);
                fragilePower.flash();
                fragilePower.amount = magicNumber;
                fragilePower.updateDescription();
            } else if (p.hasPower(SnappedPower.POWER_ID)) {
                p.getPower(SnappedPower.POWER_ID).flash();
            } else if (!p.hasPower(FragilePower.POWER_ID) && !p.hasPower(SnappedPower.POWER_ID)) {
                // apply FragilePower only if the player doesn't already have FragilePower or SnappedPower.
                addToBot(new ApplyPowerAction(p, p, new FragilePower(p, magicNumber), magicNumber));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
