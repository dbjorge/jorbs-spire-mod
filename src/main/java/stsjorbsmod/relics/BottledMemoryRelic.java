package stsjorbsmod.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.util.CardUtils;

import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;
import static stsjorbsmod.JorbsMod.makeID;
import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;
import static stsjorbsmod.patches.BottledMemoryPatch.AbstractCardMemoryFields.inBottleMemory;

public class BottledMemoryRelic extends CustomJorbsModRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String ID = makeID(BottledMemoryRelic.class);
    private boolean cardSelected = true;
    public AbstractCard card = null;

    public BottledMemoryRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return inBottleMemory::get;
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
                inBottleMemory.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }

    @Override
    public void onEquip() {
        // follow same behavior as base game bottle relics. Maybe there's some mod that adds a bottle mechanic changing card using getPurgeableCards
        CardGroup rememberCards = CardUtils.getCardsForBottling(AbstractDungeon.player.masterDeck.getPurgeableCards());
        rememberCards.group.removeIf(c -> !c.hasTag(REMEMBER_MEMORY));
        if (rememberCards.size() > 0) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(rememberCards, 1, String.format(DESCRIPTIONS[1], this.name), false, false, false, false);
        }
    }

    @Override
    public void onUnequip() {
        if (this.card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            if (cardInDeck != null) {
                inBottleMemory.set(card, false);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            inBottleMemory.set(card, true);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
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
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public boolean canSpawn() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(REMEMBER_MEMORY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractCard getCard() {
        return card.makeCopy();
    }
}
