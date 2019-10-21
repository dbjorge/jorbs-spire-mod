package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.CardsToTopOfDeckAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class PrestidigitationB extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(PrestidigitationB.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Commons/prestidigitation_b.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int CARD_DRAW = 2;
    private static final int UPGRADE_PLUS_CARD_DRAW = 1;

    public PrestidigitationB() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = CARD_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new DrawCardAction(p, this.magicNumber));
        enqueueAction(new CardsToTopOfDeckAction(p, p.hand, this.magicNumber, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARD_DRAW);
            initializeDescription();
        }
    }
}
