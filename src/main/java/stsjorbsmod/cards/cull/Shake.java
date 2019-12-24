package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Shake extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Shake.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int VULNERABLE = 1;
    private static final int UPGRADE_PLUS_CARDS = 1;
    private static final int CARDS_DRAWN = 2;

    public Shake() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARDS_DRAWN;
        urMagicNumber = baseUrMagicNumber = VULNERABLE;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        if (abstractMonster.hasPower("Vulnerable")) {
            addToBot(new DrawCardAction(magicNumber));
        }
        addToBot(new ApplyPowerAction(abstractMonster, p, new VulnerablePower(p, urMagicNumber, false)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_CARDS);
        }
    }
}
