package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.PatienceMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class BookOfTongues extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(BookOfTongues.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Uncommons/book_of_tongues.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DRAW_PER_TURN = 1;

    public BookOfTongues() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW_PER_TURN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new ApplyPowerAction(p, p, new DrawPower(p, this.magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true;
            upgradeDescription();
        }
    }
}
