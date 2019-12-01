package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.SelfExhumeFields;

public class WillAction extends AbstractGameAction {
    private static String ID = JorbsMod.makeID(WillAction.class);
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    private boolean upgraded;

    public WillAction(boolean upgraded) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;

        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (p.hand.group.size() == 0) {
                isDone = true;
                return;
            } else if(p.hand.group.size() == 1) {
                AbstractCard c = p.hand.getTopCard();
                exhaustWithBonuses(c, p.hand);
                this.isDone = true;
                return;
            } else if (!this.upgraded) {
                AbstractCard c = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                exhaustWithBonuses(c, p.hand);
                this.isDone = true;
                return;
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                tickDuration();
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            AbstractCard c = AbstractDungeon.handCardSelectScreen.selectedCards.getTopCard();
            exhaustWithBonuses(c, AbstractDungeon.handCardSelectScreen.selectedCards);

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
            return;
        }
    }

    private void exhaustWithBonuses(AbstractCard c, CardGroup startingGroup) {
        startingGroup.moveToExhaustPile(c);
        c.freeToPlayOnce = true;
        SelfExhumeFields.selfExhumeOnSnap.set(c, true);
    }
}
