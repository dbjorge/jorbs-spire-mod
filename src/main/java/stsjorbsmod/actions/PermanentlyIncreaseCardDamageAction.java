package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import stsjorbsmod.JorbsMod;

import java.util.UUID;

public class PermanentlyIncreaseCardDamageAction extends AbstractGameAction {
    private UUID cardUuid;
    private int damageIncrease;

    public PermanentlyIncreaseCardDamageAction(UUID cardUuid, int damageIncrease) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.cardUuid = cardUuid;
        this.damageIncrease = damageIncrease;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = AbstractDungeon.player;

            for (AbstractCard masterCard : p.masterDeck.group) {
                if (masterCard.uuid == this.cardUuid) {
                    if (masterCard.type != AbstractCard.CardType.ATTACK) {
                        JorbsMod.logger.warn("PermanentlyIncreaseCardDamageAction: Ignoring non-attack card");
                        break;
                    }

                    if (masterCard.baseDamage <= 0) {
                        JorbsMod.logger.warn("PermanentlyIncreaseCardDamageAction: Ignoring card with <=0 baseDamage");
                        break;
                    }

                    JorbsMod.logger.info("PermanentlyIncreaseCardDamageAction: Increasing baseDamage of card");

                    masterCard.baseDamage += damageIncrease;
                    masterCard.superFlash();
                    masterCard.applyPowers();

                    for (AbstractCard instance : GetAllInBattleInstances.get(cardUuid)) {
                        instance.baseDamage += damageIncrease;
                        instance.applyPowers();
                    }

                    break;
                }
            }

            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }
}
