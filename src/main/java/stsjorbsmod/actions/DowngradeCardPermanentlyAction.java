package stsjorbsmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.DowngradeableCard;
import stsjorbsmod.effects.GradeChangeShineEffect;
import stsjorbsmod.util.EffectUtils;

/**
 * Downgrade implementation assumes that we are planning on downgrading only Jorbs mod game cards. If this assumption is
 * wrong, potentially find the card from the various cardgroups and create an unupgraded instance of the card using the
 * same uuid. For this mod at the moment, Wrath stacks would likely need to carry over properly.
 * Nice to have: handle downgrading cards from other mods with unknown effects.
 * Downgrade implementation also assumes that we can't downgrade beyond base card (see Dicey Dungeons card downgrades).
 */
public class DowngradeCardPermanentlyAction extends AbstractGameAction {
    private AbstractCard card;

    public DowngradeCardPermanentlyAction(AbstractCard downgradeableCard) {
        this.card = downgradeableCard;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startDuration = this.duration = 0.25F;
    }

    @Override
    public void update() {
        if (duration == startDuration && !card.purgeOnUse && (card instanceof DowngradeableCard) && card.upgraded) {
            JorbsMod.logger.info("DowngradeCardAction downgrading " + card.toString());
            ((DowngradeableCard) card).downgrade();
            EffectUtils.addDowngradeEffect(card, duration);

            AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
            if (masterCard != null) {
                ((DowngradeableCard) masterCard).downgrade();
            } else {
                // we somehow tried to downgrade a card we added into the deck afterward.
                JorbsMod.logger.info("Card to downgrade is not in the deck: " + card.cardID);
            }
        }
        tickDuration();
    }
}
