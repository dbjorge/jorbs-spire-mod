package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;

public class EnergyBulb extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(EnergyBulb.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int ENERGY_GAIN = 1;
    private static final int UPGRADE_PLUS_ENERGY_GAIN = 1;

    public EnergyBulb() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        bannerImageRarity = CardRarity.UNCOMMON;
        magicNumber = baseMagicNumber = ENERGY_GAIN;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_ENERGY_GAIN);
            upgradeDescription();
        }
    }
}
