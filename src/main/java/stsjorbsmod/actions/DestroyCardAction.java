package stsjorbsmod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import stsjorbsmod.JorbsMod;

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
                CardCrawlGame.sound.play("CARD_EXHAUST");
                PurgeCardEffect purgeCardEffect = new PurgeCardEffect(masterCard, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2));
                purgeCardEffect.duration = purgeCardEffect.startingDuration = 0.7f;
                AbstractDungeon.topLevelEffects.add(purgeCardEffect);
                AbstractDungeon.player.masterDeck.removeCard(masterCard);
            } else {
                JorbsMod.logger.info("Failed to purge a card we didn't have. Perhaps Duplication Potion or Attack Potion or similar effect occurred. " + card.cardID);
            }
        }

        tickDuration();
    }
}
