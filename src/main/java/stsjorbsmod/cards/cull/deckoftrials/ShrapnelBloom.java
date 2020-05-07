package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.powers.ShrapnelBloomPower;

public class ShrapnelBloom extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ShrapnelBloom.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int ADDITIONAL_PLAYS = 1;
    private static final int UPGRADE_EXTRA_PLAYS = 1;


    public ShrapnelBloom() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = ADDITIONAL_PLAYS;

        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        this.addToBot(new ApplyPowerAction(p, p, new ShrapnelBloomPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_EXTRA_PLAYS);
            upgradeDescription();
        }
    }
}
