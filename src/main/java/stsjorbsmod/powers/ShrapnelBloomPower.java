package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class ShrapnelBloomPower extends CustomJorbsModPower implements OnDamageToRedirectSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(ShrapnelBloomPower.class);
    public static final String POWER_ID = STATIC.ID;

    public ShrapnelBloomPower(final AbstractCreature owner, final int numberTurns) {
        super(STATIC);

        this.owner = owner;
        this.amount = numberTurns;
        this.isTurnBased = true;

        updateDescription();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && this.amount > 0 && !card.hasTag(LEGENDARY)) {
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            --this.amount;
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "ShrapnelBloomPower"));
            }
        }

    }

    public void atEndOfRound() {
        if (this.amount == 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, "ShrapnelBloomPower"));
        } else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, "ShrapnelBloomPower", 1));
        }

    }

    @Override
    public AbstractPower makeCopy() { return new ShrapnelBloomPower(owner, amount); }

    @Override
    public boolean onDamageToRedirect(AbstractPlayer player, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        return false;
    }
}