package stsjorbsmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

// Based on BaseMod ConsoleTargetedPower
public class TargettingArrowUi
{
    public TargettingArrowUi()
    {
        this.isHidden = false;
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = new Vector2();
        }
    }

    private Vector2 from = new Vector2();
    private Vector2 to = new Vector2();
    private boolean atValidTarget;

    public void update(float fromX, float fromY, float toX, float toY, boolean atValidTarget) {
        from.x = fromX;
        from.y = fromY;
        to.x = toX;
        to.y = toY;
        this.atValidTarget = atValidTarget;
    }

    private float arrowScale;
    private float arrowScaleTimer;
    private Vector2[] points = new Vector2[20];
    private boolean isHidden;
    private int amount;

    private void close()
    {
        this.isHidden = true;
    }

    public void render(SpriteBatch sb)
    {
        if (!this.isHidden) {
            renderTargetingUi(sb);
        }
    }

    public void renderTargetingUi(SpriteBatch sb)
    {
        if (atValidTarget) {
            this.arrowScale = Settings.scale;
            this.arrowScaleTimer = 0.0F;
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        } else {
            this.arrowScaleTimer += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
            if (this.arrowScaleTimer > 1.0F) {
                this.arrowScaleTimer = 1.0F;
            }

            this.arrowScale = com.badlogic.gdx.math.Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, this.arrowScaleTimer);
            sb.setColor(new Color(1.0F, 0.2F, 0.3F, 1.0F));
        }
        Vector2 tmp = new Vector2(this.from.x - to.x, this.from.y - to.y);
        tmp.nor();

        drawCurvedLine(sb, new Vector2(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY - 40.0F * Settings.scale), new Vector2(to.x, to.y), this.from);

        sb.draw(ImageMaster.TARGET_UI_ARROW, to.x - 128.0F, to.y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.arrowScale, this.arrowScale, tmp.angle() + 90.0F, 0, 0, 256, 256, false, false);

    }

    private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control)
    {
        float radius = 7.0F * Settings.scale;

        for (int i = 0; i < this.points.length - 1; i++) {
            this.points[i] = ((Vector2) com.badlogic.gdx.math.Bezier.quadratic(this.points[i], i / 20.0F, start, control, end, new Vector2()));
            radius += 0.4F * Settings.scale;

            float angle;

            if (i != 0) {
                Vector2 tmp = new Vector2(this.points[(i - 1)].x - this.points[i].x, this.points[(i - 1)].y - this.points[i].y);
                angle = tmp.nor().angle() + 90.0F;
            } else {
                Vector2 tmp = new Vector2(this.from.x - this.points[i].x, this.from.y - this.points[i].y);
                angle = tmp.nor().angle() + 270.0F;
            }

            sb.draw(ImageMaster.TARGET_UI_CIRCLE, this.points[i].x - 64.0F, this.points[i].y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, angle, 0, 0, 128, 128, false, false);
        }
    }
}
