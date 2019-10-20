package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainMemoryClarityAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.PatienceMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Entangle extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Entangle.class.getSimpleName());
    public static final String IMG = makeCardPath("AOE_Rares/entangle.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int VULNERABLE = 0;
    private static final int UPGRADE_PLUS_VULNERABLE = 1;

    public Entangle() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = VULNERABLE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster _) {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.hasPower(SlowPower.POWER_ID)) {
                enqueueAction(new ApplyPowerAction(m, p, new SlowPower(m, 0)));
            }
            if (magicNumber > 0) {
                enqueueAction(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
            }
        }

        enqueueAction(new RememberSpecificMemoryAction(new PatienceMemory(p, false)));
        enqueueAction(new GainMemoryClarityAction(p));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
            upgradeDescription();
        }
    }
}
