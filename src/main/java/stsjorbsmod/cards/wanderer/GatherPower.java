package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.PlayNextAttackThisTurnAdditionalTimesPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class GatherPower extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(GatherPower.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Rares/gather_power.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DRAW = 2;
    private static final int NEXT_ATTACK_ADDITIONAL_TIMES = 1;
    private static final int UPGRADE_PLUS_NEXT_ATTACK_ADDITIONAL_TIMES = 1;

    public GatherPower() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
        metaMagicNumber = baseMetaMagicNumber = NEXT_ATTACK_ADDITIONAL_TIMES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new ApplyPowerAction(p, p, new PlayNextAttackThisTurnAdditionalTimesPower(p, metaMagicNumber), metaMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_NEXT_ATTACK_ADDITIONAL_TIMES);
            upgradeDescription();
        }
    }
}
