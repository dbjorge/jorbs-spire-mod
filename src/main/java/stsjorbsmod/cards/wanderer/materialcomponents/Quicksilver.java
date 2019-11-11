package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.AdvanceRelicsThroughTimeAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.PlayNextAttackThisTurnAdditionalTimesPower;

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
    private static final int NEXT_ATTACK_ADDITIONAL_TIMES = 1;
    private static final int UPGRADE_PLUS_NEXT_ATTACK_ADDITIONAL_TIMES = 1;

    public Quicksilver() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = NEXT_ATTACK_ADDITIONAL_TIMES;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AdvanceRelicsThroughTimeAction(p, RELIC_COUNTER_INCREMENT));
        addToBot(new ApplyPowerAction(p, p, new PlayNextAttackThisTurnAdditionalTimesPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_NEXT_ATTACK_ADDITIONAL_TIMES);
            upgradeDescription();
        }
    }
}
