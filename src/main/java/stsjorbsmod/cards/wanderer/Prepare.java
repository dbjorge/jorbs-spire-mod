package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.CoilPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Prepare extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Prepare.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int COIL = 4;
    private static final int UPGRADE_PLUS_COIL = 2;

    public Prepare() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = COIL;
        baseBlock = 0;
    }

    // Note: because this is dependent on Coil and Coil amount changes during use(), this won't be
    // accurate if used as part of the card description. So don't.
    @Override
    protected int calculateBonusBaseBlock() {
        AbstractPower possibleCoilPower = AbstractDungeon.player.getPower(CoilPower.POWER_ID);
        int existingCoil = (possibleCoilPower == null ? 0 : possibleCoilPower.amount);
        return existingCoil;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CoilPower(p, p, magicNumber)));

        // We can't use a GainBlockAction directly because we need to recalculate block in applyPowers in between the
        // new Coil stacks applying, this card being played (and possibly adding a new coil stack for that), and the
        // GainBlockAction being scheduled.
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                Prepare.this.applyPowers();
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, Prepare.this.block));
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_COIL);
            upgradeDescription();
        }
    }
}
