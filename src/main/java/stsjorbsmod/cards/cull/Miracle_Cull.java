package stsjorbsmod.cards.cull;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Miracle_Cull extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Miracle_Cull.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int ENERGY_GAIN = 1;
    private static final int UPGRADE_ENERGY_GAIN = 1;
    private static final int MANIFEST_INCREASE = 1;

    public Miracle_Cull() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        AlwaysRetainField.alwaysRetain.set(this, true);
        magicNumber = baseMagicNumber = ENERGY_GAIN;
        metaMagicNumber = baseMetaMagicNumber = MANIFEST_INCREASE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new GainEnergyAction(magicNumber));
        if (p instanceof Cull) {
            ((Cull) AbstractDungeon.player).manifest += metaMagicNumber;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_ENERGY_GAIN);
            upgradeDescription();
        }
    }
}
