package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.FieldAccess;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.BurningPower;

public class RenderBurningBlockPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
    public static class AbstractCreature_renderBlockIconAndValue_showFire {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().contains(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        m.replace(String.format("{ $_ = $proceed($$); " +
                                        "if (this.hasPower(\"%1$s\")) {" +
                                        "%2$s.renderFireOnBlock(sb, x, y, %3$s#BLOCK_ICON_X, %3$s#BLOCK_ICON_Y, this.blockOffset);" +
                                        "} }",
                                JorbsMod.makeID(BurningPower.class.getSimpleName()),
                                RenderBurningBlockPatch.class.getName(),
                                AbstractCreature.class.getName()));
                    }
                }

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractCreature.class.getName()) && f.getFieldName().equals("blockColor")) {
                        f.replace(String.format("{ $_ = %2$s.renderBurningBlock(this.hasPower(\"%1$s\"), $proceed()); }",
                                JorbsMod.makeID(BurningPower.class.getSimpleName()),
                                RenderBurningBlockPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static void renderFireOnBlock(SpriteBatch sb, float x, float y, float BLOCK_ICON_X, float BLOCK_ICON_Y, float blockOffset) {
        sb.setColor(Color.GOLD.cpy().sub(0.0F, 0.0F, 0.0F, 0.2F));
        sb.draw(ImageMaster.ATK_FIRE, x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 28.0F + blockOffset, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F);
    }

    public static Color renderBurningBlock(boolean isBurning, Color blockColor) {
        if (isBurning) {
            return blockColor.cpy().lerp(Color.RED.cpy(), 0.2F);
        } else {
            return blockColor;
        }
    }
}
