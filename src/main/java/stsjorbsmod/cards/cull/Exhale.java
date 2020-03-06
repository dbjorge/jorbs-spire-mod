package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.LoseStrengthNextTurnPower;

public class Exhale extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Exhale.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int INTANGIBLE = 1;
    private static final int STRENGTH_LOSS = 1;

    public Exhale() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = INTANGIBLE;
        urMagicNumber = baseUrMagicNumber = STRENGTH_LOSS;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, urMagicNumber)));
        addToBot(new ApplyPowerAction(p, p, new LoseStrengthNextTurnPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeDescription();
            EphemeralField.ephemeral.set(this, false);
            this.isEthereal = false;
            this.exhaust = true;
        }
    }
}
