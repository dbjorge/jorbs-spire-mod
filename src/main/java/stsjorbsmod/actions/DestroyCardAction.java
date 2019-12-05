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
        JorbsMod.logger.info("DestroyCardAction purging " + card.toString());
        CardCrawlGame.sound.play("CARD_EXHAUST");
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
        AbstractDungeon.player.masterDeck.removeCard(card);
        isDone = true;
    }
}
