package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Smite;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Blasphemer extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Blasphemer.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = COST_UNPLAYABLE;
    private static final int SMITE_AMOUNT = 4;

    public Blasphemer() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.cardsToPreview = new Smite();
        magicNumber = baseMagicNumber = SMITE_AMOUNT;
    }

    @Override
    public void triggerWhenDrawn() {
        if (upgraded) {
            AbstractCard s = new Smite();
            s.upgrade();
            this.addToBot(new MakeTempCardInHandAction(s, this.magicNumber));
        }
        else
            this.addToBot(new MakeTempCardInHandAction(new Smite(), this.magicNumber));
        this.addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.cardsToPreview.upgrade();
            this.upgradeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }
}
