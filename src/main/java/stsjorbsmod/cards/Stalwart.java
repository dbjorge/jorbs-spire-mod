package stsjorbsmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.CardsToTopOfDeckAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.SlothMemory;
import stsjorbsmod.memories.TemperanceMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class Stalwart extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Stalwart.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/stalwart.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 1; // yes, higher

    public Stalwart() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new RememberSpecificMemoryAction(new TemperanceMemory(p, upgraded)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}
