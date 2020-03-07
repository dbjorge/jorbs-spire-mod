package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Pollute extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Pollute.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;

    public Pollute() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        for (AbstractCard c : p.hand.group) {
            if (c != this) {
                c.type = CardType.CURSE;
            }
        }
    }

    @Override
    public void upgrade() {
        this.exhaust = false;
        upgradeName();
        upgradeDescription();

    }
}
