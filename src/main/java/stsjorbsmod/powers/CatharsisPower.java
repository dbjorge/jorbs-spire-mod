package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.BlueCandle;

public class CatharsisPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(CatharsisPower.class);
    public static final String POWER_ID = STATIC.ID;

    private static final int HP_LOSS = 1;

    public CatharsisPower(final AbstractCreature owner, final int damagePerCurse) {
        super(STATIC);

        this.owner = owner;
        this.amount = damagePerCurse;
        this.amount2 = 0;
        updateDescription();
    }

    // Curses are made playable in PlayableCursesPatch

    public void onInitialApplication(){
        this.amount2 = (int)AbstractDungeon.actionManager.cardsPlayedThisCombat
                .stream()
                .filter(c -> c.type.equals(AbstractCard.CardType.CURSE))
                .count();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.CURSE) {
            this.flash();

            // this ensures you don't lose 1 HP twice when you have both CatharsisPower and Blue Candle
            if (!AbstractDungeon.player.hasRelic(BlueCandle.ID)) {
                this.addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, HP_LOSS, AbstractGameAction.AttackEffect.FIRE));
            }
            ++amount2;
            for (int i = 0; i < amount2; ++i) {
                this.addToBot(new DamageRandomEnemyAction(new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
            }
        }
    }

    @Override
    public void updateDescription() {
            this.description = this.amount2 == 1 ? String.format(DESCRIPTIONS[1], this.amount, this.amount2, HP_LOSS) : String.format(DESCRIPTIONS[0], this.amount, this.amount2, HP_LOSS);

    }

    @Override
    public AbstractPower makeCopy() {
        return new CatharsisPower(owner, amount);
    }
}

