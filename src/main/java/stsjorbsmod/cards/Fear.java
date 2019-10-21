package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.FearPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Fear extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Fear.class.getSimpleName());
    public static final String IMG = makeCardPath("Bad_Rares/fear.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int FEAR_DURATION = 3;
    private static final int UPGRADE_PLUS_FEAR_DURATION = -1;

    public Fear() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = FEAR_DURATION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new ApplyPowerAction(m, p, new FearPower(m, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_FEAR_DURATION);
            upgradeDescription();
        }
    }
}
