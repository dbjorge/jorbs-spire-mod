package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.TemperanceMemory;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class Stalwart extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Stalwart.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;

    public Stalwart() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, TemperanceMemory.STATIC.ID));
        if (upgraded) {
            addToBot(new GainClarityOfCurrentMemoryAction(p));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
