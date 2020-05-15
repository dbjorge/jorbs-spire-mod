package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class OldBook extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(OldBook.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = COST_UNPLAYABLE;
    private static final int HEAL_PERCENT = 0;
    private static final int UPGRADE_HEAL_PERCENT = 10;

    public OldBook() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = HEAL_PERCENT;

        tags.add(LEGENDARY);
    }

    private int countCursesInGroup(CardGroup group) {
        return (int) group.group.stream().filter(c -> c.type.equals(CardType.CURSE)).count();
    }

    private void exhaustCursesInGroup(CardGroup group) {
        for (AbstractCard c : group.group) {
            if (c.type.equals(CardType.CURSE)) {
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(c, group));
            }
        }
    }

    @Override
    public int calculateBonusMagicNumber() {
        AbstractPlayer p = AbstractDungeon.player;
        return countCursesInGroup(p.hand) +
                countCursesInGroup(p.drawPile) +
                countCursesInGroup(p.discardPile);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        exhaustCursesInGroup(p.hand);
        exhaustCursesInGroup(p.drawPile);
        exhaustCursesInGroup(p.discardPile);

        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void triggerWhenDrawn() {
            addToTop(new ApplyPowerAction();
    }

    @Override
    public void triggerOnExhaust() {
        addToBot(new RemoveSpecificPowerAction());
    }

    @Override
    public void triggerOnManualDiscard() {
        addToBot(new RemoveSpecificPowerAction());
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_HEAL_PERCENT);
            upgradeDescription();
        }
    }
}
