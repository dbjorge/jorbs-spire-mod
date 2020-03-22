package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Anxiety extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Anxiety.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int INITIAL_DRAW = 3;
    private static final int UPGRADE_DRAW = 1;

    private static final int ENERGY = 1;
    private static final int DRAW_LOSS = 1;

    public Anxiety() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMetaMagicNumber = metaMagicNumber = INITIAL_DRAW;
        this.misc = metaMagicNumber;
        baseMagicNumber = magicNumber = DRAW_LOSS;
        baseUrMagicNumber = urMagicNumber = ENERGY;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new DrawCardAction(this.misc));
        addToBot(new GainEnergyAction(this.urMagicNumber));
        if (this.misc > 0) {
            this.addToBot(new IncreaseMiscAction(this.uuid, this.misc, -this.magicNumber));
        }
    }

    public void applyPowers() {
        this.baseMetaMagicNumber = this.misc;
        super.applyPowers();
        this.initializeDescription();
    }

    @Override
    public void applyLoadedMiscValue(int misc) {
        this.baseMetaMagicNumber = this.misc;
        super.applyLoadedMiscValue(misc);
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMetaMagicNumber(UPGRADE_DRAW);
            this.misc += UPGRADE_DRAW;

            upgradeName();
            upgradeDescription();
        }
    }
}
