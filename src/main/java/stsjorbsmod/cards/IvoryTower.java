package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ExhaustCardsMatchingPredicateAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

// 0: Draw 2(4) cards, exhaust all curses and statuses in your hand.
public class IvoryTower extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(IvoryTower.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Rares/ivory_tower.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int DRAW = 2;
    private static final int UPGRADE_PLUS_DRAW = 2;

    public IvoryTower() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new DrawCardAction(p, magicNumber, false));
        enqueueAction(new ExhaustCardsMatchingPredicateAction(p, p.hand,
                c -> c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
        }
    }
}
