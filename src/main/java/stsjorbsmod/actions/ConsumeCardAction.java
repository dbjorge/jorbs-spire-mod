package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.relics.SpiritPoop;
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

    public ConsumeCardAction(AbstractCard c) {
        this.card = c;
    }

    public void update() {
        switch (card.rarity) {
            case CURSE:
                if (!AbstractDungeon.player.hasRelic(SpiritPoop.ID)) {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, RelicLibrary.getRelic("Spirit Poop").makeCopy());
                } else {
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F, new Circlet());
                }
                break;

            case BASIC:
                AbstractDungeon.player.heal(BASIC_HEAL);
                break;
            case COMMON:
                AbstractDungeon.player.increaseMaxHp(COMMON_HP_GAIN, false);
                break;

            case SPECIAL:
                if (card.hasTag(JorbsMod.JorbsCardTags.LEGENDARY)) {
                    AbstractDungeon.player.increaseMaxHp(LEGENDARY_HP_GAIN, false);
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
                }
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
        AbstractDungeon.actionManager.addToBottom(new ConsumerGameAction<>(CardMetaUtils::removeCard, card));
        AbstractDungeon.actionManager.addToBottom(new ConsumerGameAction<>(EffectUtils::showDestroyEffect, card));
        isDone = true;
    }
}
