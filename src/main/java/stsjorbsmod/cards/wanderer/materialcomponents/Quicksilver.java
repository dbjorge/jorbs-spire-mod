package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.AdvanceRelicsThroughTimeAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Quicksilver extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Quicksilver.class.getSimpleName());
    public static final String IMG = makeCardPath("Material_Components/quicksilver.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int RELIC_COUNTER_INCREMENT = 1;
    private static final int DOUBLE_TAP_TURNS = 1;
    private static final int UPGRADE_PLUS_DOUBLE_TAP_TURNS = 1;

    public Quicksilver() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = RELIC_COUNTER_INCREMENT;
        metaMagicNumber = baseMetaMagicNumber = DOUBLE_TAP_TURNS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AdvanceRelicsThroughTimeAction(p, magicNumber));
        addToBot(new ApplyPowerAction(p, p, new DoubleTapPower(p, metaMagicNumber), metaMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_DOUBLE_TAP_TURNS);
            upgradeDescription();
        }
    }
}
