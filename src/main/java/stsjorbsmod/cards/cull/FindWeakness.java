package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.FindWeaknessPower;

public class FindWeakness extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FindWeakness.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int SCRY_AMOUNT = 2;
    private static final int UPGRADE_SCRY_AMOUNT = 1;


    public FindWeakness() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = SCRY_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FindWeaknessPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_SCRY_AMOUNT);
            upgradeDescription();
        }
    }
}
