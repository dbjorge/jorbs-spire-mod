package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.SnappedPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class LoseGrip extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(LoseGrip.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Rares/withdraw.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int BLOCK = 12;
    private static final int INTANGIBLE = 1;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public LoseGrip() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = INTANGIBLE;
        retain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(SnappedPower.POWER_ID)) {
            enqueueAction(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, this.magicNumber), this.magicNumber));
        } else {
            enqueueAction(new GainBlockAction(p, p, block));
        }
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
