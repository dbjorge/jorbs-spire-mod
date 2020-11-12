package stsjorbsmod.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.actions.ExhumeCardsAction;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.patches.SelfExhumeFields;
import stsjorbsmod.util.CardUtils;

import java.util.Iterator;
import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.makeID;
import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;
import static stsjorbsmod.patches.BottledBurdenPatch.AbstractCardMemoryFields.inBottledBurden;

public class BottledBurdenRelic extends CustomJorbsModRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String ID = makeID(BottledBurdenRelic.class);
    public AbstractCard card = null;
    public static final int EXHUME_TURN = 3;
    private boolean cardSelected = true;

    public BottledBurdenRelic(){super(ID, CULL_CARD_COLOR, RelicTier.COMMON, LandingSound.CLINK);}

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return inBottledBurden::get;
    }

    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                inBottledBurden.set(card, true);
                AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
                EntombedField.entombed.set(cardInDeck, true);
                setDescriptionAfterLoading();
            }
        }
    }

    @Override
    public void onEquip() {
        // follow same behavior as base game bottle relics.
        CardGroup nonExertCards = CardUtils.getCardsForBottling(AbstractDungeon.player.masterDeck.getPurgeableCards());
        nonExertCards.group.removeIf(c -> SelfExertField.selfExert.get(c));
        if (nonExertCards.size() > 0) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(nonExertCards, 1, String.format(DESCRIPTIONS[1], this.name), false, false, false, false);
        }
    }
    @Override
    public void onUnequip() {
        if (this.card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            if (cardInDeck != null) {
                inBottledBurden.set(card, false);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            inBottledBurden.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            setDescriptionAfterLoading();

            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            EntombedField.entombed.set(cardInDeck, true);
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = String.format(DESCRIPTIONS[2], EXHUME_TURN,  card.name);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (!this.grayscale) {
            ++this.counter;
        }

        if (this.counter == 3 && cardSelected) {
            this.flash();
            addToBot(new ExhumeCardsAction(card));
            this.counter = -1;
            this.grayscale = true;
        }
    }

    @Override
    public boolean canSpawn() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (!SelfExertField.selfExert.get(c))
                return true;
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], EXHUME_TURN);
    }

    public AbstractCard getCard() {
        return card.makeCopy();
    }
}
