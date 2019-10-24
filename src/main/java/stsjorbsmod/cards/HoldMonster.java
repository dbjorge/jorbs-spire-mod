package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainMemoryClarityAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.TemperanceMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class HoldMonster extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(HoldMonster.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Rares/hold_monster.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int STRENGTH_PENALTY = 6;

    public HoldMonster() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STRENGTH_PENALTY;

        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            addToBot(new RememberSpecificMemoryAction(new TemperanceMemory(p, true)));
        } else {
            addToBot(new GainMemoryClarityAction(p, TemperanceMemory.STATIC.ID));
            addToBot(new RememberSpecificMemoryAction(new TemperanceMemory(p, false)));
        }

        AbstractGameAction action = new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber);
        addToBot(action);
        if (m != null && !m.hasPower(ArtifactPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
