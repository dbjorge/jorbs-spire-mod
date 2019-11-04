package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class BookOfTongues extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(BookOfTongues.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Uncommons/book_of_tongues.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DRAW_PER_TURN = 1;
    private static final int UPGRADE_PLUS_DRAW_PER_TURN = 1;

    public BookOfTongues() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW_PER_TURN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DrawPower(p, this.magicNumber)));

        // TODO: this should be adding a "draw from memory deck at start of turn" effect instead of increasing normal
        //  cards drawn, but we haven't implemented the memory deck yet.
        /*
        if (upgraded) {
            addToBot(new ApplyPowerAction(p, p, new MakeTempCardInHandAction(MemoryDeck.createRandomCard())));
        }
        */
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW_PER_TURN);
            upgradeDescription();
        }
    }
}
