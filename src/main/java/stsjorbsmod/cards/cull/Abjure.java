package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GetCardFromDrawPileAction;
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

        for (AbstractCard c : p.drawPile.group) {
            if (c instanceof SpiritShield_Cull) {
                addToBot(new GetCardFromDrawPileAction(c));
            }
        }
        for (AbstractCard c : p.discardPile.group) {
            if (c instanceof SpiritShield_Cull) {
                addToBot(new DiscardToHandAction(c));
            }
        }
        for (AbstractCard c : p.exhaustPile.group) {
            if (c instanceof SpiritShield_Cull) {
                addToBot(new ExhaustToHandAction(c));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.selfRetain = true;
            upgradeName();
            upgradeDescription();
        }
    }
}
