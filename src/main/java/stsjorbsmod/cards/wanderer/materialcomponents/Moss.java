package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

public class Moss extends MaterialComponent {
    public static final String ID = JorbsMod.makeID(Moss.class);
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int DEXTERITY = 3;
    private static final int UPGRADE_PLUS_DEXTERITY = 1;

    public Moss() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEXTERITY;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void useMaterialComponent(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEXTERITY);
            upgradeDescription();
        }
    }
}
