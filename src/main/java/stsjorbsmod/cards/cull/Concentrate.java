package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Concentrate extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Concentrate.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;

    private static final int ENERGY_GAIN = 2;
    private static final int DISCARD = 3;
    private static final int UPGRADE_PLUS_DISCARD = -1;

    public Concentrate() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = DISCARD;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscardAction(p, p, this.magicNumber, false));
        this.addToBot(new GainEnergyAction(ENERGY_GAIN));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DISCARD);
            upgradeDescription();
        }
    }
}
