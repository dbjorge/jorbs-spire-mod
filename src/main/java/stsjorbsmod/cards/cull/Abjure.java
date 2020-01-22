package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.AbjureAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Abjure extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Abjure.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;

    public Abjure() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new AbjureAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            selfRetain = true;

            upgradeName();
            upgradeDescription();
        }
    }
}
