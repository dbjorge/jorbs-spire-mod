package stsjorbsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.cards.cull.SpiritShield_Cull;

public class AbjureAction extends AbstractGameAction {
    private boolean showHandFull = false;

    public AbjureAction() {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }
        this.duration = this.startDuration;
    }

    private int moveSpiritShieldsToHand(CardGroup originalPile) {
        int movedCards = 0;
        for (AbstractCard c : originalPile.group) {
            if (c instanceof SpiritShield_Cull) {
                ++movedCards;
                AbstractDungeon.actionManager.addToBottom(new PileToHandAction(originalPile, c));
            }
        }
        return movedCards;
    }

    @Override
    public void update() {
        int movedCards = 0;
        AbstractPlayer p = AbstractDungeon.player;

        if (this.duration == this.startDuration) {
            movedCards += moveSpiritShieldsToHand(p.drawPile);
            movedCards += moveSpiritShieldsToHand(p.discardPile);
            movedCards += moveSpiritShieldsToHand(p.exhaustPile);

            if (movedCards + p.hand.size() > BaseMod.MAX_HAND_SIZE) {
                showHandFull = true;
            }
        }

        this.tickDuration();

        if (isDone) {
            if (showHandFull) {
                p.createHandIsFullDialog();
            }
        }
    }
}
