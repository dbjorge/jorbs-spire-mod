package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class MistyStep extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MistyStep.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/misty_step.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int BLOCK_PER_CLARITY = 1;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public MistyStep() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK_PER_CLARITY;
        magicNumber = baseMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int calculatedBlock = MemoryManager.forPlayer(p).countCurrentClarities() * block;
        addToBot(new GainBlockAction(p, p, calculatedBlock));
        addToBot(new DrawCardAction(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            initializeDescription();
        }
    }
}
