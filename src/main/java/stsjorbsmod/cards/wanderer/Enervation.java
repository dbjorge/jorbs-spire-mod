package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.GainBlockPerPoisonAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Enervation extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Enervation.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/enervation.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int POISON = 5;
    private static final int UPGRADE_PLUS_POISON = 2;

    public Enervation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = POISON;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber)));
        addToBot(new GainBlockPerPoisonAction(p, m));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POISON);
            initializeDescription();
        }
    }
}
