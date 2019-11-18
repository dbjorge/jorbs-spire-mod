package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnDrawCardSubscriber;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class CorrodingBarrier extends CustomJorbsModCard implements OnDrawCardSubscriber {
    public static final String ID = JorbsMod.makeID(CorrodingBarrier.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Rares/corroding_barrier.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int BLOCK = 23;
    private static final int BLOCK_LOSS_PER_DRAW = 3;
    private static final int UPGRADE_PLUS_BLOCK_LOSS_PER_DRAW = -3;

    public CorrodingBarrier() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = BLOCK_LOSS_PER_DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void onDraw() {
        final AbstractPlayer p = AbstractDungeon.player;

        if (this.magicNumber > 0) {
            addToBot(new ModifyBlockAction(this.uuid, -this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK_LOSS_PER_DRAW);
            upgradeDescription();
        }
    }
}
