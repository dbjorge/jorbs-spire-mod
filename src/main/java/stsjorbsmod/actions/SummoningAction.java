package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.SummoningPower;

public class SummoningAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(SummoningAction.class);
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(UI_ID).TEXT;

    private AbstractPlayer player;
    private int numberOfCards;

    public SummoningAction(int numberOfCards) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (!this.player.drawPile.isEmpty() && this.numberOfCards > 0) {
                CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);

                for (AbstractCard c : this.player.drawPile.group) {
                    temp.addToTop(c);
                }

                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);

                String msg = this.numberOfCards == 1 ? TEXT[0] : TEXT[1];
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, msg);

                this.tickDuration();
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                    this.player.drawPile.moveToExhaustPile(c);
                    addToBot(new ApplyPowerAction(player, player, new SummoningPower(player, c)));
                }
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }
}
