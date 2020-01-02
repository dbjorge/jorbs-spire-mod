package stsjorbsmod.cards.cull;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
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
<<<<<<< HEAD
            //until beta releases, replace the following line with the commented line
            AlwaysRetainField.alwaysRetain.set(this, true);
//            this.selfRetain = true;
=======
            // TODO: Replace with selfRetain on beta release
            AlwaysRetainField.alwaysRetain.set(this, true);

>>>>>>> 5910d3a55380113ce4c944c3716024e4cbc16d28
            upgradeName();
            upgradeDescription();
        }
    }
}
