package stsjorbsmod.cards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

// X: Discard your hand and lose all Block. Gain 4 energy and draw 6 cards. Gain 1 Weak. Exhaust.
// (Upgrade removes the Weak power.)
public class TimeWalk extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(TimeWalk.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Rares/time_walk.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = -1;
    private static final int ENERGY = 4;
    private static final int DRAW = 6;
    private static final int WEAK = 1;
    private static final int UPGRADE_PLUS_WEAK = -1;

    public TimeWalk() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = ENERGY;
        urMagicNumber = baseUrMagicNumber = DRAW;
        metaMagicNumber = baseMetaMagicNumber = WEAK;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new LoseEnergyAction(EnergyPanel.getCurrentEnergy()));
        enqueueAction(new DiscardAction(p, p, BaseMod.MAX_HAND_SIZE, false));
        enqueueAction(new RemoveAllBlockAction(p, p));
        enqueueAction(new GainEnergyAction(magicNumber));
        enqueueAction(new DrawCardAction(p, urMagicNumber, false));
        if (metaMagicNumber > 0) {
            enqueueAction(new ApplyPowerAction(p, p, new WeakPower(p, metaMagicNumber, false)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_WEAK);
            upgradeDescription();
        }
    }
}
