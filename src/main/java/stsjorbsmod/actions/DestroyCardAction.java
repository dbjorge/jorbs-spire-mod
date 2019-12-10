package stsjorbsmod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.EffectUtils;

public class DestroyCardAction extends AbstractGameAction {
    AbstractCard card;

    public DestroyCardAction(AbstractCard card) {
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = 0.25F;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            card.purgeOnUse = true; // handles destroying the copy that's in the middle of being played
            AbstractDungeon.player.discardPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
            AbstractDungeon.player.drawPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
            AbstractDungeon.player.exhaustPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
            AbstractDungeon.player.hand.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));

            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
            if (masterCard != null) {
                JorbsMod.logger.info("DestroyCardAction purging " + masterCard.toString());
                AbstractDungeon.player.masterDeck.removeCard(masterCard);
            } else {
                JorbsMod.logger.info("Failed to purge a card we didn't have. Perhaps Duplication Potion or Attack Potion or similar effect occurred. " + card.cardID);
            }
        }

        tickDuration();
        if(this.isDone) {
            EffectUtils.showDestroyEffect(card);
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
    }
}
