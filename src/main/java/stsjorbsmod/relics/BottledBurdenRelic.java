package stsjorbsmod.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.cards.wanderer.ForbiddenGrimoire;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.SelfExhumeFields;
import stsjorbsmod.powers.EntombedGrimoirePower;
import stsjorbsmod.util.CardUtils;
import stsjorbsmod.powers.ExhumeOnTurnXPower;

import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.JorbsCardTags.HAS_EXERT;
import static stsjorbsmod.JorbsMod.makeID;
import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;
import static stsjorbsmod.patches.BottledBurdenPatch.AbstractCardMemoryFields.inBottledBurden;

public class BottledBurdenRelic extends CustomJorbsModRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String ID = makeID(BottledBurdenRelic.class);
    public AbstractCard card = null;
    public static final int EXHUME_TURN = 3;
    private String exhumeOnTurnXPowerInstanceID;
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
                setDescriptionAfterLoading();
            }
        }
    }

    @Override
    public void onEquip() {
        // follow same behavior as base game bottle relics.
        CardGroup nonExertCards = CardUtils.getCardsForBottling(AbstractDungeon.player.masterDeck.getPurgeableCards());
        nonExertCards.group.removeIf(c -> c.hasTag(HAS_EXERT));
        if (nonExertCards.size() > 0) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(nonExertCards, 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, false, false, false);
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
            this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();

            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            EntombedField.entombed.set(cardInDeck, true);
            SelfExhumeFields.selfExhumeOnTurnX.set(cardInDeck, true);
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        AbstractPower exhumeOnTurnXPower = new ExhumeOnTurnXPower(AbstractDungeon.player, AbstractDungeon.player.masterDeck.getSpecificCard(this.card), EXHUME_TURN);
        exhumeOnTurnXPowerInstanceID = exhumeOnTurnXPower.ID;
        //addToTop(new ExhaustSpecificCardAction(this.card, AbstractDungeon.player.drawPile));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, exhumeOnTurnXPower));
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractCard getCard() {
        return card.makeCopy();
    }
}
