package stsjorbsmod.cards.cull;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.WastingEssencePower;

public class WastingEssence extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(WastingEssence.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 3;
    private static final int NUMBER_OF_CURSES = 5;
    private static final int DAMAGE_PER_CURSE = 20;
    private static final int UPGRADE_DAMAGE_PER_CURSE = 10;

    public WastingEssence() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = DAMAGE_PER_CURSE;
        metaMagicNumber = baseMetaMagicNumber = NUMBER_OF_CURSES;

        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new WastingEssencePower(p, magicNumber, metaMagicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE_PER_CURSE);
            upgradeDescription();
        }
    }
}
