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
import stsjorbsmod.powers.OldBookPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class OldBook extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(OldBook.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private AbstractPlayer p = AbstractDungeon.player;

    private static final int COST = COST_UNPLAYABLE;
    private static final int HEAL_PERCENT = 0;
    private static final int UPGRADE_HEAL_PERCENT = 10;

    public OldBook() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = HEAL_PERCENT;

        tags.add(LEGENDARY);
    }

    @Override
    public void triggerWhenDrawn() {
            addToTop(new ApplyPowerAction(p, p, new OldBookPower(AbstractDungeon.player, this)));
    }

    @Override
    public void triggerOnExhaust() {
        addToBot(new RemoveSpecificPowerAction(p, p, OldBookPower.POWER_ID));
    }

    @Override
    public void triggerOnManualDiscard() {
        addToBot(new RemoveSpecificPowerAction(p, p, OldBookPower.POWER_ID));
    }

//    @Override
//    public void onMoveToDiscardImpl() {
//        addToBot(new RemoveSpecificPowerAction(p, p, OldBookPower.POWER_ID));
//        super.onMoveToDiscardImpl();
//    }

    @Override
    public boolean canUse(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) { return false; }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
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
