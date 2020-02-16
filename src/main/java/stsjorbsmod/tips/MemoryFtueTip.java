package stsjorbsmod.tips;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import stsjorbsmod.effects.SnapTurnCounterEffect;
import stsjorbsmod.memories.*;

import static stsjorbsmod.JorbsMod.makeID;

public class MemoryFtueTip extends FtueTip {
    public static boolean shouldFakeBeingRemembered(AbstractMemory m) {
        return
                AbstractDungeon.screen == AbstractDungeon.CurrentScreen.FTUE &&
                AbstractDungeon.ftue != null &&
                AbstractDungeon.ftue instanceof MemoryFtueTip &&
                m.ID.equals(PatienceMemory.STATIC.ID);
    }

    public static boolean shouldFakeBeingClarified(AbstractMemory m) {
        return
                AbstractDungeon.screen == AbstractDungeon.CurrentScreen.FTUE &&
                AbstractDungeon.ftue != null &&
                AbstractDungeon.ftue instanceof MemoryFtueTip &&
                (m.ID.equals(WrathMemory.STATIC.ID) || m.ID.equals(DiligenceMemory.STATIC.ID));
    }

    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID(MemoryFtueTip.class)).TEXT;

    public static boolean trigger(float snapUiCenterX, float snapUiCenterY) {
        if (JorbsModTipTracker.shouldShow(JorbsModTipTracker.TipKey.MEMORY)) {
            AbstractDungeon.ftue = new MemoryFtueTip(snapUiCenterX + 550, snapUiCenterY);
            AbstractDungeon.dynamicBanner.hide();
            JorbsModTipTracker.neverShowAgain(JorbsModTipTracker.TipKey.MEMORY);
            return true;
        }
        return false;
    }

    private MemoryFtueTip(float x, float y) {
        super(TEXT[0], TEXT[1], x, y, TipType.COMBAT);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        MemoryManager.forPlayer().render(sb);
    }
}
