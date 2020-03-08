package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Accumulation extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Accumulation.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DRAW_AMOUNT = 2;
    private static final int UPGRADE_DRAW_AMOUNT = 1;

    public Accumulation() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = DRAW_AMOUNT;
    }

    @Override
    public int calculateBonusMagicNumber() {
        long nbAccumulationInDiscard = AbstractDungeon.player.discardPile.group
               .stream()
               .filter(c -> c.cardID.equals(Accumulation.ID))
               .count();

        return (int) nbAccumulationInDiscard;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeMagicNumber(UPGRADE_DRAW_AMOUNT);
            upgradeName();
            upgradeDescription();
        }
    }
}
