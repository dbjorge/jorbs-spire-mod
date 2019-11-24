package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class MistyStep extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MistyStep.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int BLOCK_PER_CLARITY = 1;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int DRAW = 1;

    public MistyStep() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = 0;
        magicNumber = baseMagicNumber = DRAW;
        metaMagicNumber = baseMetaMagicNumber = BLOCK_PER_CLARITY;
    }

    @Override
    protected int calculateBonusBaseBlock() {
        return MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities() * metaMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new DrawCardAction(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeDescription();
        }
    }
}
