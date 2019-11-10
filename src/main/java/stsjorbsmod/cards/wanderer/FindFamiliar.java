package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.FindFamiliarPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class FindFamiliar extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FindFamiliar.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Uncommons/find_familiar.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE_PER_TURN_AFTER_SNAP = 0;
    private static final int UPGRADE_PLUS_DAMAGE_PER_TURN_AFTER_SNAP = 10;

    public FindFamiliar() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DAMAGE_PER_TURN_AFTER_SNAP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FindFamiliarPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PER_TURN_AFTER_SNAP);
            upgradeDescription();
        }
    }
}
