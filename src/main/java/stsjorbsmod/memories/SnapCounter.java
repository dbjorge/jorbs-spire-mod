package stsjorbsmod.memories;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.effects.SnapTurnCounterEffect;
import stsjorbsmod.powers.FragilePower;
import stsjorbsmod.tips.MemoryFtueTip;
import stsjorbsmod.tips.SnapFtueTip;

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

    public static final int SNAP_TURN = 7;

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

    public int currentTurn; // we track this separately from the game manager to avoid ordering issues with start-of-turn triggers
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
        if (isSnapTurn()) {
            tips.get(0).body = String.format(TEXT[2]);
        } else {
            tips.get(0).body = String.format(TEXT[1], currentTurn);
        }
    }

    public void atPreBattle() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new FragilePower(owner, this)));
    }

    public void atStartOfTurn() {
        currentTurn++;
        alpha = MathUtils.lerp(STARTING_ALPHA, ENDING_ALPHA, currentTurn / (float) SNAP_TURN);
        updateDescription();

        boolean showingMemoryFtue = MemoryFtueTip.trigger(centerX, centerY);
        if (!showingMemoryFtue) {
            SnapFtueTip.trigger(centerX, centerY);
        }
    }

    public void forceSnapTurn() {
        currentTurn = SNAP_TURN;
        alpha = MathUtils.lerp(STARTING_ALPHA, ENDING_ALPHA, currentTurn / 7.0F);
        tips.get(0).body = String.format(TEXT[2]);
    }

    public void atEndOfTurn() {
        if (isSnapTurn()) {
            AbstractDungeon.actionManager.addToBottom(new SnapAction(owner, true));
        }
    }

    public boolean isSnapTurn() {
        return currentTurn == SNAP_TURN;
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

        int currentTurn = SnapFtueTip.shouldFakeCurrentTurn() ? SnapFtueTip.fakeCurrentTurn() : this.currentTurn;

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
                if (currentTurn >= SNAP_TURN) {
                    color = colors[i % SNAP_TURN];
                }
                float indicatorAngle = 360.0F * (((float)i) / (currentTurn));
                indicatorAngle += flipMultiplier * (360.0F * (indicatorCircleRotationTimer / INDICATOR_CIRCLE_ROTATION_DURATION));
                float x = flipMultiplier * INDICATOR_CIRCLE_X_RADIUS * MathUtils.cosDeg(indicatorAngle) + centerX;
                float y = flipMultiplier * INDICATOR_CIRCLE_Y_RADIUS * MathUtils.sinDeg(indicatorAngle) + centerY;

                float scaleModifier = isSnapTurn() ? 1.6F : 1.0F;
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
