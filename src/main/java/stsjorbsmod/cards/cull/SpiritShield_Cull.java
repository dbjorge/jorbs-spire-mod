package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.SpiritShieldPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class SpiritShield_Cull extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(SpiritShield_Cull.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE_REDUCTION = 1;
    private static final int NUMBER_OF_TURNS = 1;

    public SpiritShield_Cull() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DAMAGE_REDUCTION;
        urMagicNumber = baseUrMagicNumber = NUMBER_OF_TURNS;
        exhaust = true;

        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new SpiritShieldPower(p, urMagicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            exhaust = false;
            upgradeName();
            upgradeDescription();
        }
    }
}
