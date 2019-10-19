package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.AdvanceRelicsThroughTimeAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Haste extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Haste.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Rares/haste.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int RELIC_COUNTER_INCREMENT = 1;
    private static final int DOUBLE_TAP_TURNS = 1;
    private static final int UPGRADE_PLUS_DOUBLE_TAP_TURNS = 1;

    public Haste() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DOUBLE_TAP_TURNS;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new AdvanceRelicsThroughTimeAction(p, RELIC_COUNTER_INCREMENT));
        enqueueAction(new ApplyPowerAction(p, p, new DoubleTapPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DOUBLE_TAP_TURNS);
            initializeDescription();
        }
    }
}
