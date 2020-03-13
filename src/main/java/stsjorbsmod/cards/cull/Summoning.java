package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.SummoningAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Summoning extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Summoning.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int NUMBER_OF_CARDS = 1;
    private static final int UPGRADE_NUMBER_OF_CARDS = 1;


    public Summoning() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = NUMBER_OF_CARDS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new SummoningAction(magicNumber, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMagicNumber(UPGRADE_NUMBER_OF_CARDS);

            upgradeName();
            upgradeDescription();
        }
    }
}
