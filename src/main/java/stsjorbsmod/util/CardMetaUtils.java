package stsjorbsmod.util;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumerGameAction;
import stsjorbsmod.cards.DowngradeableCard;
import stsjorbsmod.patches.ExertedField;
import stsjorbsmod.patches.LegendaryPatch;
import stsjorbsmod.patches.SelfExertField;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class CardMetaUtils {
    /**
     * Downgrade implementation assumes that we are planning on downgrading only Jorbs mod game cards. If this assumption is
     * wrong, potentially find the card from the various cardgroups and create an unupgraded instance of the card using the
     * same uuid. For this mod at the moment, Wrath stacks would likely need to carry over properly.
     * Nice to have: handle downgrading cards from other mods with unknown effects.
     * Downgrade implementation also assumes that we can't downgrade beyond base card (see Dicey Dungeons card downgrades).
     */
    public static void downgradeCardPermanently(AbstractCard card) {
        if (!card.purgeOnUse && (card instanceof DowngradeableCard) && card.upgraded) {
            JorbsMod.logger.info("Downgrading " + card.toString());
            ((DowngradeableCard) card).downgrade();

            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
            if (masterCard != null) {
                ((DowngradeableCard) masterCard).downgrade();
            } else {
                // we somehow tried to downgrade a card we added into the deck afterward.
                JorbsMod.logger.info("Card to downgrade is not in the deck: " + card.cardID);
            }
        }
    }

    public static void destroyCardPermanently(AbstractCard card) {
        card.purgeOnUse = true; // handles destroying the copy that's in the middle of being played
        AbstractDungeon.player.discardPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
        AbstractDungeon.player.drawPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
        AbstractDungeon.player.exhaustPile.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));
        AbstractDungeon.player.hand.group.removeIf(abstractCard -> abstractCard.uuid.equals(card.uuid));

        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if (masterCard != null) {
            JorbsMod.logger.info("Removing " + masterCard.toString());
            AbstractDungeon.player.masterDeck.removeCard(masterCard);

            if (masterCard.hasTag(LEGENDARY)) {
                JorbsMod.logger.info(String.format("destroyCardPermanently: re-adding legendary card %1$s to pools", masterCard.toString()));
                LegendaryPatch.addCardToPools(masterCard.makeCopy());
            }
        } else {
            JorbsMod.logger.info("Failed to purge a card we didn't have. Perhaps Duplication Potion or Attack Potion or similar effect occurred. " + card.cardID);
        }
    }

    // Based on Burst/DoubleTap
    public static void playCardAdditionalTime(AbstractCard card, AbstractMonster target) {
        AbstractCard tmp = card.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(tmp);
        tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tmp.target_y = (float)Settings.HEIGHT / 2.0F;
        if (tmp.cost > 0) {
            tmp.freeToPlayOnce = true;
        }

        if (target != null) {
            tmp.calculateCardDamage(target);
        }

        tmp.purgeOnUse = true;

        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, target, card.energyOnUse, true, true), true);
    }
}
