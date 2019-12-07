package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.wanderer.Patron;

import java.util.Optional;

public class DestroyCardAction extends AbstractGameAction {
    AbstractCard card;
    public DestroyCardAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        // In the highly unlikely event that UUIDs clash and the found card isn't actually the specific card... oops
        Optional<AbstractCard> cardOp = AbstractDungeon.player.masterDeck.group.stream().filter(c -> c.uuid == card.uuid).findAny();
        if(cardOp.isPresent()) {
            AbstractCard masterCard = cardOp.get();
            JorbsMod.logger.info("DestroyCardAction purging " + masterCard.toString());
            CardCrawlGame.sound.play("CARD_EXHAUST");
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(masterCard, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(masterCard);
        } else {
            JorbsMod.logger.info("Failed to purge a card we didn't have. Perhaps Duplication Potion or Attack Potion or similar effect occurred.");
        }
        isDone = true;
    }
}
