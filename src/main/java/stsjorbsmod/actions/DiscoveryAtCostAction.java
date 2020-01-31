package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import stsjorbsmod.patches.ExtraCopiesToAddWhenGeneratingCardField;

import java.util.ArrayList;

public class DiscoveryAtCostAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private AbstractCard.CardType cardType;
    private ArrayList<AbstractCard> cardChoices;
    private boolean isSkippable;

    public DiscoveryAtCostAction(ArrayList<AbstractCard> cardChoices, boolean isSkippable) {
        this.cardChoices = cardChoices;
        this.isSkippable = isSkippable;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(cardChoices, CardRewardScreen.TEXT[1], isSkippable);
        } else {
            if (!retrieveCard) {
                if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                    int copiesToGenerate = 1 + ExtraCopiesToAddWhenGeneratingCardField.field.get(AbstractDungeon.cardRewardScreen.discoveryCard);
                    for (int i = 0; i < copiesToGenerate; ++i) {
                        AbstractCard discoveredCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                        discoveredCard.current_x = -1000.0F * Settings.scale;
                        if (AbstractDungeon.player.hand.size() < 10) {
                            AbstractDungeon.effectList.add(
                                    new ShowCardAndAddToHandEffect(discoveredCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        } else {
                            AbstractDungeon.effectList.add(
                                    new ShowCardAndAddToDiscardEffect(discoveredCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                        }
                    }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                }
                retrieveCard = true;
            }
        }
        tickDuration();
    }
}
