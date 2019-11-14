package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.LustMemory;
import stsjorbsmod.patches.AutoExhumeField;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Inferno extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Inferno.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Uncommons/inferno.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BURNING_MULTIPLIER = 2;
    private static final int UPGRADE_PLUS_BURNING_MULTIPLIER = 1;

    public Inferno() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURNING_MULTIPLIER;
        exhaust = true;
        AutoExhumeField.autoExhumeBehavior.set(this, AutoExhumeBehavior.EXHUME_ON_SNAP);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, LustMemory.STATIC.ID));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower possibleExistingBurningPower = m.getPower(BurningPower.POWER_ID);
                if (possibleExistingBurningPower != null) {
                    int stacksToAdd = possibleExistingBurningPower.amount * (magicNumber - 1);
                    addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, stacksToAdd), stacksToAdd));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURNING_MULTIPLIER);
            upgradeDescription();
        }
    }
}
