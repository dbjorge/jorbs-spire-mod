package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Strife extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Strife.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int STRENGTH_PER_CURSE = 1;
    private static final int UPGRADE_STRENGTH_PER_CURSE = 1;
    private static final int CURSE_AMOUNT = 1;

    public Strife() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = 0;
        metaMagicNumber = baseMetaMagicNumber = STRENGTH_PER_CURSE;
    }

    @Override
    public int calculateBonusMagicNumber() {
        int nbCurses = CURSE_AMOUNT;
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard card : p.drawPile.group) {
            if (card.type.equals(CardType.CURSE)) {
                ++nbCurses;
            }
        }
        for (AbstractCard card : p.discardPile.group) {
            if (card.type.equals(CardType.CURSE)) {
                ++nbCurses;
            }
        }
        for (AbstractCard card : p.hand.group) {
            if (card.type.equals(CardType.CURSE)) {
                ++nbCurses;
            }
        }
        return nbCurses * metaMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        AbstractCard c = AbstractDungeon.returnRandomCurse();
        addToBot(new MakeTempCardInDiscardAction(c, CURSE_AMOUNT));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }



    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_STRENGTH_PER_CURSE);
            upgradeDescription();
        }
    }
}
