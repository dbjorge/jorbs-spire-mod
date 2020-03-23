package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EphemeralField;

public class Haunt extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Haunt.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int INTANGIBLE = 1;
    private static final int REDUCE_DRAW = 1;

    public Haunt() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = REDUCE_DRAW;
        urMagicNumber = baseUrMagicNumber = INTANGIBLE;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, urMagicNumber)));
        addToBot(new ApplyPowerAction(p, p, new DrawReductionPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            EphemeralField.ephemeral.set(this, false);
            this.isEthereal = false;
            this.exhaust = true;

            upgradeName();
            upgradeDescription();
        }
    }
}
