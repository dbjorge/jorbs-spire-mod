package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.CardMetaUtils;
import stsjorbsmod.util.EffectUtils;

public class ConsumeCardAction extends AbstractGameAction {
    private final AbstractCard card;
    private static final int BASIC_HEAL = 5;
    private static final int COMMON_HP_GAIN = 3;
    private static final int UNCOMMON_HP_GAIN = 3;
    private static final int RARE_HP_GAIN = 5;
    private static final int LEGENDARY_HP_GAIN = 5;
    private static final int CURSE_HP_LOSS = 1;


    public ConsumeCardAction(AbstractCard c) {
        this.card = c;
    }

    public void update() {
        if (card.hasTag(JorbsMod.JorbsCardTags.LEGENDARY)) {
            AbstractDungeon.player.increaseMaxHp(LEGENDARY_HP_GAIN, false);
            AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
        } else {
            switch (card.rarity) {
                case CURSE:
                    AbstractDungeon.player.decreaseMaxHealth(CURSE_HP_LOSS);
                    break;
                case BASIC:
                    AbstractDungeon.player.heal(BASIC_HEAL);
                    break;
                case COMMON:
                    AbstractDungeon.player.increaseMaxHp(COMMON_HP_GAIN, false);
                    break;

                case SPECIAL:
                    break;

                case UNCOMMON:
                    AbstractDungeon.player.increaseMaxHp(UNCOMMON_HP_GAIN, false);
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                    break;

                case RARE:
                    AbstractDungeon.player.increaseMaxHp(RARE_HP_GAIN, false);
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                    break;

            }
        }
        AbstractDungeon.actionManager.addToBottom(new ConsumerGameAction<>(CardMetaUtils::destroyCardPermanently, card));
        AbstractDungeon.actionManager.addToBottom(new ConsumerGameAction<>(EffectUtils::showDestroyEffect, card));
        isDone = true;
    }
}
