package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class DisguiseSelf extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(DisguiseSelf.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Commons/disguise_self.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;

    public DisguiseSelf() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.block), this.block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
