package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.IOnDrawCardSubscriber;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.SnappedPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class CorrodingBarrier extends CustomJorbsModCard implements IOnDrawCardSubscriber {
    public static final String ID = JorbsMod.makeID(CorrodingBarrier.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Rares/corroding_barrier.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 2;
    private static final int BLOCK = 23;
    private static final int BLOCK_LOSS_PER_DRAW = 3;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int UPGRADE_PLUS_BLOCK_LOSS_PER_DRAW = -1;
    private static final int HP_LOSS_PER_SNAPPED_DRAW = 2;

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

        AbstractGameAction action = new ModifyBlockAction(this.uuid, -this.magicNumber);
        addToBot(action);

        if (p.hasPower(SnappedPower.POWER_ID)) {
            flash();
            addToBot(new LoseHPAction(p, p, HP_LOSS_PER_SNAPPED_DRAW, AttackEffect.POISON));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK_LOSS_PER_DRAW);
            initializeDescription();
        }
    }
}
