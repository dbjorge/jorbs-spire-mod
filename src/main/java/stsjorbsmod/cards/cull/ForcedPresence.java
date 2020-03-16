package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class ForcedPresence extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ForcedPresence.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int INTANGIBLE_LOSS = 1;
    private static final int ENERGY_GAIN = 2;
    private static final int UPGRADE_ENERGY_GAIN = 1;

    public ForcedPresence() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        magicNumber = baseMagicNumber = INTANGIBLE_LOSS;
        urMagicNumber = baseUrMagicNumber = ENERGY_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        if (p.hasPower(IntangiblePlayerPower.POWER_ID) && p.getPower(IntangiblePlayerPower.POWER_ID).amount > 0) {
            AbstractPower power = p.getPower(IntangiblePlayerPower.POWER_ID);
            if (power.amount == 1) {
                this.addToBot(new RemoveSpecificPowerAction(p, p, power));
            } else {
                this.addToBot(new ReducePowerAction(p, p, power, magicNumber));
            }

            addToBot(new GainEnergyAction(urMagicNumber));
        }
    }

    @Override
    public boolean shouldGlowGold() {
        AbstractPlayer p = AbstractDungeon.player;
        return (p.hasPower(IntangiblePlayerPower.POWER_ID) && p.getPower(IntangiblePlayerPower.POWER_ID).amount > 0);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUrMagicNumber(UPGRADE_ENERGY_GAIN);
            upgradeDescription();
        }
    }
}
