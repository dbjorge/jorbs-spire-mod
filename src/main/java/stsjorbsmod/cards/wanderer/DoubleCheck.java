package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.DiligenceMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class DoubleCheck extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(DoubleCheck.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Commons/double_check.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int DRAW = 0;
    private static final int UPGRADE_DRAW = 1;

    public DoubleCheck() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = DRAW;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new GainClarityOfCurrentMemoryAction(p, DiligenceMemory.STATIC.ID));
        addToBot(new RememberSpecificMemoryAction(p, DiligenceMemory.STATIC.ID));
        if (upgraded) {
            addToBot(new DrawCardAction(p, magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
