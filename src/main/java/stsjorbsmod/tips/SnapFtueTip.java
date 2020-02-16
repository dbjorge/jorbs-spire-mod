package stsjorbsmod.tips;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.MultiPageFtue;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import stsjorbsmod.effects.SnapTurnCounterEffect;
import stsjorbsmod.memories.SnapCounter;

import static stsjorbsmod.JorbsMod.makeID;

public class SnapFtueTip extends FtueTip {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID(SnapFtueTip.class)).TEXT;

    public static boolean shouldFakeCurrentTurn() {
        return AbstractDungeon.screen == AbstractDungeon.CurrentScreen.FTUE && AbstractDungeon.ftue != null && AbstractDungeon.ftue instanceof SnapFtueTip;
    }

    public static int fakeCurrentTurn() {
        float timer = ((SnapFtueTip) AbstractDungeon.ftue).ftueTurnTimer;
        return MathUtils.clamp(1 + ((int) (timer / FTUE_TURN_DURATION)), 1, 7);
    }

    public static void trigger(float snapUiCenterX, float snapUiCenterY) {
        if (JorbsModTipTracker.shouldShow(JorbsModTipTracker.TipKey.SNAP)) {
            AbstractDungeon.ftue = new SnapFtueTip(snapUiCenterX + 400, snapUiCenterY);
            AbstractDungeon.dynamicBanner.hide();
            JorbsModTipTracker.neverShowAgain(JorbsModTipTracker.TipKey.SNAP);
        }
    }

    private static final float FTUE_TURN_DURATION = 1.2F;
    private float ftueTurnTimer = 0.0F;

    private SnapFtueTip(float x, float y) {
        super(TEXT[0], TEXT[1], x, y, TipType.COMBAT);
    }

    @Override
    public void render(SpriteBatch sb) {
        ftueTurnTimer += Gdx.graphics.getDeltaTime();
        if (ftueTurnTimer > FTUE_TURN_DURATION * 8) {
            ftueTurnTimer = 0.0F;
        }

        super.render(sb);
        for (AbstractGameEffect e: AbstractDungeon.effectList) {
            if (e instanceof SnapTurnCounterEffect) {
                e.render(sb);
            }
        }
    }
}
