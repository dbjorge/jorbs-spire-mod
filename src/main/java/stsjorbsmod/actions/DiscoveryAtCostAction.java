package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

public class DiscoveryAtCostAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private AbstractCard.CardType cardType = null;

    public DiscoveryAtCostAction(AbstractCard.CardType type) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        cardType = type;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            // TODO: discoveryOpen is deprecated in the beta branch. Replace this with something like:
            //     AbstractDungeon.cardRewardScreen.customCombatOpen(generateMaterialComponents(3), UI_TEXT_FOR_CHOOSE_MATERIAL_COMPONENT, true);
            AbstractDungeon.cardRewardScreen.discoveryOpen(cardType);
            tickDuration();
        } else {
            if (!retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    AbstractCard discoveredCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                    discoveredCard.current_x = -1000.0F * Settings.scale;
                    if (AbstractDungeon.player.hand.size() < 10) {
                        AbstractDungeon.effectList.add(
                                new ShowCardAndAddToHandEffect(discoveredCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    } else {
                        AbstractDungeon.effectList.add(
                                new ShowCardAndAddToDiscardEffect(discoveredCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                retrieveCard = true;
            }
            tickDuration();
        }
    }
}
