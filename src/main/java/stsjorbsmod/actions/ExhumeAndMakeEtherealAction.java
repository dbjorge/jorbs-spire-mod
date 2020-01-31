package stsjorbsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Exhume;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.CorruptionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.cull.Seance;

import java.util.ArrayList;
import java.util.Iterator;

public class ExhumeAndMakeEtherealAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final UIStrings ExhumeUiString = CardCrawlGame.languagePack.getUIString("ExhumeAction");
    public static final String[] TEXT = ExhumeUiString.TEXT;
    private static final String UI_ID = JorbsMod.makeID(ExhumeAndMakeEtherealAction.class.getSimpleName());
    private static final UIStrings prependUiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    private static final String PrependText = prependUiStrings.TEXT[0];

    private ArrayList<AbstractCard> exhumes = new ArrayList<>();

    public ExhumeAndMakeEtherealAction() {
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, this.amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.size() == BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.player.createHandIsFullDialog();
                this.isDone = true;
                return;
            }
            if (this.p.exhaustPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.p.exhaustPile.size() == 1) {

                if ((this.p.exhaustPile.group.get(0)).cardID.equals(Exhume.ID) || (this.p.exhaustPile.group.get(0)).cardID.equals(Seance.ID)) {
                    this.isDone = true;
                    return;
                }
                AbstractCard abstractCard = this.p.exhaustPile.getTopCard();
                abstractCard.unfadeOut();
                if (!abstractCard.isEthereal) {
                    abstractCard.isEthereal = true;
                    abstractCard.rawDescription = PrependText + abstractCard.rawDescription;
                    abstractCard.initializeDescription();
                }

                this.p.hand.addToHand(abstractCard);
                if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && abstractCard.type == AbstractCard.CardType.SKILL)
                    abstractCard.setCostForTurn(-9);
                this.p.exhaustPile.removeCard(abstractCard);
                abstractCard.unhover();
                abstractCard.fadingOut = false;
                this.isDone = true;
                return;
            }
            for (AbstractCard abstractCard : this.p.exhaustPile.group) {
                abstractCard.stopGlowing();
                abstractCard.unhover();
                abstractCard.unfadeOut();
            }
            for (Iterator<AbstractCard> c = this.p.exhaustPile.group.iterator(); c.hasNext(); ) {
                AbstractCard derp = c.next();
                if (derp.cardID.equals(Exhume.ID) || derp.cardID.equals(Seance.ID)) {
                    c.remove();
                    this.exhumes.add(derp);
                }
            }
            if (this.p.exhaustPile.isEmpty()) {
                this.p.exhaustPile.group.addAll(this.exhumes);
                this.exhumes.clear();
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(this.p.exhaustPile, 1, TEXT[0], false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (!c.isEthereal) {
                    c.isEthereal = true;
                    c.rawDescription = PrependText + c.rawDescription;
                    c.initializeDescription();
                }

                this.p.hand.addToHand(c);
                if (AbstractDungeon.player.hasPower(CorruptionPower.POWER_ID) && c.type == AbstractCard.CardType.SKILL)
                    c.setCostForTurn(-9);
                this.p.exhaustPile.removeCard(c);
                c.unhover();
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
            this.p.exhaustPile.group.addAll(this.exhumes);
            this.exhumes.clear();
            for (AbstractCard c : this.p.exhaustPile.group) {
                c.unhover();
                c.target_x = CardGroup.DISCARD_PILE_X;
                c.target_y = 0.0F;
            }
        }
        tickDuration();
    }
}
