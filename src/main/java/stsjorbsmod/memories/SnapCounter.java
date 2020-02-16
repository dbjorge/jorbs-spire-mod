package stsjorbsmod.memories;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.JorbsModTipTracker;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.effects.SnapTurnCounterEffect;
import stsjorbsmod.util.TargettingArrowUi;

import java.util.ArrayList;

// This class handles the Wanderer's "snap at end of turn 7" effect and UI
public class SnapCounter {
    private static final float HB_WIDTH = 80F * Settings.scale;
    private static final float HB_HEIGHT = 80F * Settings.scale;
    private static final float TIP_X_THRESHOLD = 1544.0F * Settings.scale;
    private static final float TIP_OFFSET_R_X = 20.0F * Settings.scale;
    private static final float TIP_OFFSET_L_X = -380.0F * Settings.scale;

    private static final float INDICATOR_CIRCLE_X_RADIUS = 30F * Settings.scale;
    private static final float INDICATOR_CIRCLE_Y_RADIUS = 24F * Settings.scale;
    private static final float INDICATOR_CIRCLE_ROTATION_DURATION = 20F;
    private static final float INDICATOR_PARTICLE_DURATION = .06F;
    private static final float FTUE_TURN_DURATION = 1.0F;

    private static final String UI_ID = JorbsMod.makeID(SnapCounter.class);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    private static final String[] TEXT = uiStrings.TEXT;

    private final AbstractPlayer owner;
    private final Hitbox hb;
    private final ArrayList<PowerTip> tips;

    private float centerX;
    private float centerY;
    private float indicatorCircleRotationTimer;
    private float indicatorParticleTimer;
    private float ftueTurnTimer;

    private static final Color[] colors = new Color[] {
            Color.VIOLET.cpy(),
            new Color(.24F, .45F, 1.0F, 1.0F),
            Color.SKY.cpy(),
            Color.FOREST.cpy(),
            Color.YELLOW.cpy(),
            Color.ORANGE.cpy(),
            Color.RED.cpy()
    };

    private static final float STARTING_ALPHA = 0.65F;
    private static final float ENDING_ALPHA = 1.0F;
    private float alpha;

    private int currentTurn; // we track this separately from the game manager to avoid ordering issues with start-of-turn triggers
    public boolean isActive;

    public SnapCounter(AbstractPlayer owner) {
        this.owner = owner;
        this.hb = new Hitbox(HB_WIDTH, HB_HEIGHT);

        tips = new ArrayList<>();
        tips.add(new PowerTip(TEXT[0], TEXT[1]));
        tips.add(new PowerTip(BaseMod.getKeywordTitle("stsjorbsmod:snap"), BaseMod.getKeywordDescription("stsjorbsmod:snap")));

        reset();
    }

    private void updateDescription() {
        tips.get(0).body = String.format(TEXT[1], currentTurn);
    }

    public void atStartOfTurn() {
        currentTurn++;
        alpha = MathUtils.lerp(STARTING_ALPHA, ENDING_ALPHA, currentTurn / 7.0F);
        updateDescription();

        if (JorbsModTipTracker.shouldShow(JorbsModTipTracker.TipKey.SNAP)) {
            ftueTurnTimer = 0.0F;
            AbstractDungeon.ftue = new SnapFtueTip(centerX, centerY);
            JorbsModTipTracker.neverShowAgain(JorbsModTipTracker.TipKey.SNAP);
        }
    }

    private class SnapFtueTip extends FtueTip {
        public SnapFtueTip(float x, float y) {
            super(TEXT[2], TEXT[3], x + 400, y, FtueTip.TipType.COMBAT);
        }

        @Override
        public void render(SpriteBatch sb) {
            super.render(sb);
            for (AbstractGameEffect e: AbstractDungeon.effectList) {
                if (e instanceof SnapTurnCounterEffect) {
                    e.render(sb);
                }
            }
        }
    }

    public void atEndOfTurn() {
        if (currentTurn == 7) {
            AbstractDungeon.actionManager.addToBottom(new SnapAction(AbstractDungeon.player, true));
        }
    }

    private boolean isVisible() {
        return isActive
                && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom)
                && !owner.isDead
                && !owner.isEscaping
                && !MemoryManager.forPlayer(owner).isSnapped();
    }

    public boolean isHovered() {
        return isVisible() && hb.hovered;
    }

    public void reset() {
        currentTurn = 0;
        isActive = true;
        alpha = STARTING_ALPHA;
        updateDescription();
    }

    public void update(float centerX, float centerY, int flipMultiplier) {
        if (!isVisible()) { return; }

        int currentTurn = this.currentTurn;
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.FTUE && AbstractDungeon.ftue != null && AbstractDungeon.ftue instanceof SnapFtueTip) {
            ftueTurnTimer += Gdx.graphics.getDeltaTime();
            if (ftueTurnTimer > FTUE_TURN_DURATION * 8) {
                ftueTurnTimer = 0.0F;
            }
            currentTurn = MathUtils.clamp(1 + ((int) (ftueTurnTimer / FTUE_TURN_DURATION)), 1, 7);
        }

        this.centerX = centerX;
        this.centerY = centerY;
        hb.update(centerX - hb.width / 2, centerY - hb.height / 2);

        indicatorCircleRotationTimer -= Gdx.graphics.getDeltaTime();
        if (indicatorCircleRotationTimer < 0.0) {
            indicatorCircleRotationTimer = INDICATOR_CIRCLE_ROTATION_DURATION;
        }

        indicatorParticleTimer -= Gdx.graphics.getDeltaTime();
        if (indicatorParticleTimer < 0.0) {
            indicatorParticleTimer = INDICATOR_PARTICLE_DURATION;

            Color color = colors[MathUtils.clamp(currentTurn - 1, 0, colors.length-1)];
            color.a = alpha;

            for (int i = 0; i < currentTurn; ++i) {
                if (currentTurn >= 7) {
                    color = colors[i % 7];
                }
                float indicatorAngle = 360.0F * (((float)i) / (currentTurn));
                indicatorAngle += flipMultiplier * (360.0F * (indicatorCircleRotationTimer / INDICATOR_CIRCLE_ROTATION_DURATION));
                float x = flipMultiplier * INDICATOR_CIRCLE_X_RADIUS * MathUtils.cosDeg(indicatorAngle) + centerX;
                float y = flipMultiplier * INDICATOR_CIRCLE_Y_RADIUS * MathUtils.sinDeg(indicatorAngle) + centerY;

                float scaleModifier = currentTurn == 7 ? 1.6F : 1.0F;
                AbstractDungeon.effectList.add(new SnapTurnCounterEffect(x, y, color, scaleModifier));
            }
        }
    }

    public void renderTips(SpriteBatch sb) {
        if (!isVisible()) { return; }

        // Based on the AbstractCreature.renderPowerTips impl
        float tipX = centerX + hb.width / 2.0F < TIP_X_THRESHOLD ?
                centerX + hb.width / 2.0F + TIP_OFFSET_R_X :
                centerX - hb.width / 2.0F + TIP_OFFSET_L_X;

        // The calculatedAdditionalOffset ensures everything is shifted to avoid going offscreen
        float tipY = centerY + TipHelper.calculateAdditionalOffset(tips, centerY);

        TipHelper.queuePowerTips(tipX, tipY, tips);
    }

    public void render(SpriteBatch sb) {
        // We don't currently render anything directly; it's indirect via the effects added in update()
    }
}
