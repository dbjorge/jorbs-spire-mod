package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class WakingDream extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(WakingDream.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int EXTRA_COST_PER_RETAIN = 1;
    private static final int INTANGIBLE = 1;
    private static final int UPGRADE_PLUS_INTANGIBLE = 1;

    public WakingDream() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        selfRetain = true;
        exhaust = true;
        magicNumber = baseMagicNumber = INTANGIBLE;
        metaMagicNumber = baseMetaMagicNumber = EXTRA_COST_PER_RETAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber)));
    }

    @Override
    public void onRetained() {
        ++this.cost;
        ++this.costForTurn;
        this.isCostModified = true;
        this.isCostModifiedForTurn = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_INTANGIBLE);
            upgradeDescription();
        }
    }
}
