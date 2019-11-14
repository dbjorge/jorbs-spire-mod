package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.LustMemory;
import stsjorbsmod.patches.AutoExhumeField;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class SiphonEnergy extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(SiphonEnergy.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Rares/siphon_energy.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;

    public SiphonEnergy() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower possibleExistingBurningPower = m.getPower(BurningPower.POWER_ID);
        if (possibleExistingBurningPower != null) {
            int strengthToAdd = possibleExistingBurningPower.amount;
            addToBot(new RemoveSpecificPowerAction(m, p, possibleExistingBurningPower));

            if (strengthToAdd > 0) {
                addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, strengthToAdd), strengthToAdd));
                addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, strengthToAdd), strengthToAdd));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
            upgradeDescription();
        }
    }
}
