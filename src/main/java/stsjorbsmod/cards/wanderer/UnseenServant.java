package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.CardsToTopOfDeckAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.SlothMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class UnseenServant extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(UnseenServant.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Commons/unseen_servant.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int CARD_MOVE_COUNT = 2;
    private static final int DRAW = 0;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public UnseenServant() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_MOVE_COUNT;
        metaMagicNumber = baseMetaMagicNumber = DRAW;
        exhaust = true;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, SlothMemory.STATIC.ID));
        addToBot(new CardsToTopOfDeckAction(p, p.discardPile, this.magicNumber, false));
        if (metaMagicNumber > 0) {
            addToBot(new DrawCardAction(p, metaMagicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_DRAW);
            upgradeDescription();
        }
    }
}
