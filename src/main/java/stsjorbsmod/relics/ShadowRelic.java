package stsjorbsmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import stsjorbsmod.JorbsMod;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

/**
 * The first time you exert a card each combat, gain 1 Intangible.
 * TODO: relic stats mod support
 */
public class ShadowRelic extends CustomJorbsModRelic {
    public static final String ID = JorbsMod.makeID(ShadowRelic.class);

    private static final int TURNS = 1;
    private static boolean usedThisCombat = false;

    public ShadowRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atPreBattle() {
        usedThisCombat = false;
        pulse = true;
        beginPulse();
    }

    @Override
    public void onTrigger() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !usedThisCombat) {
            flash();
            pulse = false;
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCreature p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,new IntangiblePlayerPower(p, TURNS)));
            usedThisCombat = true;
            grayscale = true;
        }
    }

    public void justEnteredRoom(AbstractRoom room) {
        grayscale = false;
    }

    public void onVictory() {
        this.pulse = false;
    }

    public AbstractRelic makeCopy() {
        return new ShadowRelic();
    }
}
