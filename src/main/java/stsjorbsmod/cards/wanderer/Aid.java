package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.KindnessMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class Aid extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Aid.class.getSimpleName());
    public static final String IMG = makeCardPath("Bad_Uncommons/aid.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int ENEMY_HEAL = 5;

    public Aid() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ENEMY_HEAL;
        exhaust = true;
        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(m, p, magicNumber));
        addToBot(new RememberSpecificMemoryAction(p, KindnessMemory.STATIC.ID));
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
