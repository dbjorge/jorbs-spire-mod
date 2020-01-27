package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.MirrorPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class Mirror extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mirror.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 3;
    private static final int NUMBER_OF_TURNS = 1;
    private static final int UPGRADED_COST = 2;

    public Mirror() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        urMagicNumber = baseUrMagicNumber = NUMBER_OF_TURNS;
        tags.add(LEGENDARY);
        EphemeralField.ephemeral.set(this, true);

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new MirrorPower(p, urMagicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}
