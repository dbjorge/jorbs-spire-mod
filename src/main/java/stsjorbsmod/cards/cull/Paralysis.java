package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Paralysis extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Paralysis.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static final int INTANGIBLE = 1;

    public Paralysis() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.selfRetain = true;
        magicNumber = baseMagicNumber = INTANGIBLE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber), magicNumber));
    }

    @Override
    public void onRetained() {
        this.addToBot(new ReduceCostAction(this));
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            upgradeDescription();
        }
    }
}
