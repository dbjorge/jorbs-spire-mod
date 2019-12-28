package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WastingEssencePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(WastingEssencePower.class);
    public static final String POWER_ID = STATIC.ID;

    private int numberOfCurses;

    public WastingEssencePower(AbstractCreature owner, int damagePerCurse, int numberOfCurses) {
        super(STATIC);

        this.owner = owner;
        this.amount = damagePerCurse;
        this.numberOfCurses = numberOfCurses;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE) {
            this.flash();
            addToBot(new DamageAllEnemiesAction(this.owner, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON, true));
        }
    }

    @Override
    public void onInitialApplication() {
        for (int i = 0; i < numberOfCurses; i++) {
            AbstractCard c = AbstractDungeon.returnRandomCurse();
            addToBot(new MakeTempCardInDiscardAction(c, 1));
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new WastingEssencePower(owner, amount, numberOfCurses);
    }
}
