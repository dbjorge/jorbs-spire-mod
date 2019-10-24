package stsjorbsmod.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.ImmuneToDamagePower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Banish extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Banish.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Rares/banish.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int DURATION = 2;

    public Banish() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = DURATION;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StunMonsterAction(m, p, magicNumber));
        if (!m.hasPower(ArtifactPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new ImmuneToDamagePower(m, magicNumber), magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
