package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.BurningPower;

public class Flame extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Flame.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int BURNING_AMOUNT = 10;
    private static final int ENEMY_VULNERABLE = 2;
    private static final int UPGRADE_PLUS_BURNING = 2;
    private static final int UPGRADE_PLUS_VULNERABLE = 1;


    public Flame() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURNING_AMOUNT;
        urMagicNumber = baseUrMagicNumber = ENEMY_VULNERABLE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, magicNumber, false)));
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, urMagicNumber, false)));

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURNING);
            upgradeUrMagicNumber(UPGRADE_PLUS_VULNERABLE);
            upgradeDescription();
        }
    }
}
