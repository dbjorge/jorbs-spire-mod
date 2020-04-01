package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Cleanse extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Cleanse.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int HEAL = 1;
    private static final int UPGRADE_HEAL = 1;


    public Cleanse() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = 0;
        baseUrMagicNumber = urMagicNumber = HEAL;
        this.exhaust = true;

        tags.add(CardTags.HEALING);
    }

    @Override
    public int calculateBonusMagicNumber() {
        int numberOfCurses = 0;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type.equals(CardType.CURSE)) {
                ++numberOfCurses;
            }
        }
        return numberOfCurses;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numberOfCurses = 0;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type.equals(CardType.CURSE)) {
                addToBot(new DiscardSpecificCardAction(c, AbstractDungeon.player.drawPile));
                ++numberOfCurses;
            }
        }
        JorbsMod.logger.info("Number of Curses: " + numberOfCurses);
        if (numberOfCurses > 0) {
            addToBot(new HealAction(p, p, numberOfCurses * this.urMagicNumber));
            JorbsMod.logger.info("urMagicNumber: " + this.urMagicNumber);
            JorbsMod.logger.info(("Heal triggered"));
        }
    }

    @Override
    public boolean shouldGlowGold() {
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type.equals(CardType.CURSE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return this.magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUrMagicNumber(UPGRADE_HEAL);
            upgradeDescription();
        }
    }
}
