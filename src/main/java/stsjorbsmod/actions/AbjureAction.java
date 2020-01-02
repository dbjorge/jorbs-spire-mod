package stsjorbsmod.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.cards.cull.SpiritShield_Cull;

public class AbjureAction extends AbstractGameAction {
    private int SpiritShieldCount = 0;
    private boolean showHandFull = false;

    public AbjureAction() {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }
        this.duration = this.startDuration;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.duration == this.startDuration) {
            for (AbstractCard c : p.drawPile.group) {
                if (c instanceof SpiritShield_Cull) {
                    ++SpiritShieldCount;
                    addToBot(new GetCardFromDrawPileAction(c));
                }
            }
            for (AbstractCard c : p.discardPile.group) {
                if (c instanceof SpiritShield_Cull) {
                    ++SpiritShieldCount;
                    addToBot(new DiscardToHandAction(c));
                }
            }
            for (AbstractCard c : p.exhaustPile.group) {
                if (c instanceof SpiritShield_Cull) {
                    ++SpiritShieldCount;
                    addToBot(new ExhaustToHandAction(c));
                }
            }
            if (SpiritShieldCount + p.hand.size() > BaseMod.MAX_HAND_SIZE) {
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
