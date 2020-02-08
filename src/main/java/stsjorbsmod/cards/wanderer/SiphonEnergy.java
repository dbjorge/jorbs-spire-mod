package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.BurningPower;

public class SiphonEnergy extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(SiphonEnergy.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int CARD_DRAW = 3;
    private static final int UPGRADE_PLUS_CARD_DRAW = 2;
    private static final int BASE_ENERGY_GAIN = 0;
    private static final int ENERGY_GAIN_PER_CLARITY = 1;

    public SiphonEnergy() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        metaMagicNumber = baseMetaMagicNumber = CARD_DRAW;
        magicNumber = baseMagicNumber = BASE_ENERGY_GAIN;
        exhaust = true;
    }

    @Override
    protected int calculateBonusMagicNumber() {
        return MemoryManager.forPlayer().countCurrentClarities() * ENERGY_GAIN_PER_CLARITY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, metaMagicNumber));
        addToBot(new GainEnergyAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_CARD_DRAW);
            upgradeDescription();
        }
    }
}
