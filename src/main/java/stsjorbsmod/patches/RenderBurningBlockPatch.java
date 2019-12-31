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
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.BurningPower;

public class RenderBurningBlockPatch {
    @SpirePatch(clz = AbstractCreature.class, method = "renderHealth")
    public static class AbstractCreature_renderHealthPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().contains(AbstractCreature.class.getName()) && m.getMethodName().equals("renderBlockIconAndValue")) {
                        m.replace(String.format("{ if (this.hasPower(\"%1$s\")) {" +
                                        "%2$s.renderBurningBlock($$, %3$s#BLOCK_ICON_X, %3$s#BLOCK_ICON_Y, this.blockOffset);" +
                                        "} $_ = $proceed($$); }",
                                JorbsMod.makeID(BurningPower.class.getSimpleName()),
                                RenderBurningBlockPatch.class.getName(),
                                AbstractCreature.class.getName()));
                    }
                }
            };
        }
    }


    public static void renderBurningBlock(SpriteBatch sb, float x, float y, float BLOCK_ICON_X, float BLOCK_ICON_Y, float blockOffset) {
        sb.setColor(Color.GOLD.cpy());
        sb.draw(ImageMaster.ATK_FIRE, x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 32.0F + blockOffset, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F);
    }
}
