package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import stsjorbsmod.cards.cull.Seance;

import java.util.ArrayList;
import java.util.Iterator;

public class ExhumeAndMakeEtherealAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private String prependDescription;

    private ArrayList<AbstractCard> exhumes = new ArrayList<>();


    public ExhumeAndMakeEtherealAction(String prependDescription) {
        this.p = AbstractDungeon.player;
        this.prependDescription = prependDescription;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        Iterator<AbstractCard> c;
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.size() == 10) {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;

            } else if (this.p.exhaustPile.isEmpty()) {
                this.isDone = true;

            } else if (this.p.exhaustPile.size() == 1) {
                if ((this.p.exhaustPile.group.get(0)).cardID.equals(Exhume.ID) || (this.p.exhaustPile.group.get(0)).cardID.equals(Seance.ID)) {
                    this.isDone = true;
                } else {

                    card = this.p.exhaustPile.getTopCard();
                    card.unfadeOut();
                    if (!card.isEthereal) {
                        card.isEthereal = true;
                        card.rawDescription = prependDescription + card.rawDescription;
                        card.initializeDescription();
                    }
                    this.p.hand.addToHand(card);
                    if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && card.type == CardType.SKILL) {
                        card.setCostForTurn(-9);
                    }
                    this.p.exhaustPile.removeCard(card);

                    card.unhover();
                    card.fadingOut = false;
                    this.isDone = true;
                }

            } else {
                c = this.p.exhaustPile.group.iterator();

                while (c.hasNext()) {
                    card = c.next();
                    card.stopGlowing();
                    card.unhover();
                    card.unfadeOut();
                }

                c = this.p.exhaustPile.group.iterator();


                while (c.hasNext()) {
                    card = c.next();
                    if (card.cardID.equals(Exhume.ID) || card.cardID.equals(Seance.ID)) {
                        c.remove();
                        this.exhumes.add(card);
                    }
                }

                if (this.p.exhaustPile.isEmpty()) {
                    this.p.exhaustPile.group.addAll(this.exhumes);
                    this.exhumes.clear();
                    this.isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(this.p.exhaustPile, 1, TEXT[0], false);
                    this.tickDuration();
                }
            }

        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                for (c = AbstractDungeon.gridSelectScreen.selectedCards.iterator(); c.hasNext(); card.unhover()) {
                    card = c.next();
                    if (!card.isEthereal) {
                        card.isEthereal = true;
                        card.rawDescription = prependDescription + card.rawDescription;
                        card.initializeDescription();
                    }
                    this.p.hand.addToHand(card);
                    if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && card.type == CardType.SKILL) {
                        card.setCostForTurn(-9);
                    }
                    this.p.exhaustPile.removeCard(card);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
                this.p.exhaustPile.group.addAll(this.exhumes);
                this.exhumes.clear();

                for (c = this.p.exhaustPile.group.iterator(); c.hasNext(); card.target_y = 0.0F) {
                    card = c.next();
                    card.unhover();
                    card.target_x = (float) CardGroup.DISCARD_PILE_X;
                }
            }

            this.tickDuration();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhumeAction");
        TEXT = uiStrings.TEXT;
    }
}
