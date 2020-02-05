package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.CardsToTopOfDeckAction;
import stsjorbsmod.characters.Wanderer;

public class Index extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Index.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARD_DRAW = 3;
    private static final int TOP_OF_DECK = 3;
    private static final int UPGRADE_PLUS_TOP_OF_DECK = -1;

    public Index() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_DRAW;
        metaMagicNumber = baseMetaMagicNumber = TOP_OF_DECK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, this.magicNumber));
        addToBot(new CardsToTopOfDeckAction(p, p.hand, this.metaMagicNumber, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_TOP_OF_DECK);
            upgradeDescription();
        }
    }
}
