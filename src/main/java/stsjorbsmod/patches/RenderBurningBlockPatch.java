package stsjorbsmod.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.BurningPower;
import stsjorbsmod.util.ReflectionUtils;

public class RenderBurningBlockPatch {

    static TextureAtlas.AtlasRegion BURNING_TEXTURE = new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("128/attackBurn");
    static float burningRotation = 0.0F;

    @SpirePatch(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
    public static class AbstractCreature_renderBlockIconAndValue_showFire {
        @SpirePrefixPatch
        public static void patch(AbstractCreature __this, SpriteBatch sb, float x, float y) {
            if (__this.hasPower(BurningPower.POWER_ID)) {
                sb.setColor(Color.WHITE.cpy());
                sb.draw(BURNING_TEXTURE,
                        x + (Float) ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "BLOCK_ICON_X") - 32.0F,
                        y + (Float) ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "BLOCK_ICON_Y") - 32.0F + (Float) ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "blockOffset"),
                        32.0F,
                        32.0F,
                        64.0F,
                        64.0F,
                        Settings.scale,
                        Settings.scale,
                        burningRotation);
                burningRotation += 0.1;
            }
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
//                @Override
//                public void edit(MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().contains(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
//                        m.replace(String.format("{ $_ = $proceed($$); " +
//                                        "if (this.hasPower(\"%1$s\")) {" +
//                                        "%2$s.renderFireOnBlock(sb, x, y, %3$s#BLOCK_ICON_X, %3$s#BLOCK_ICON_Y, this.blockOffset);" +
//                                        "} }",
//                                JorbsMod.makeID(BurningPower.class.getSimpleName()),
//                                RenderBurningBlockPatch.class.getName(),
//                                AbstractCreature.class.getName()));
//                    }
//                }

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractCreature.class.getName()) && f.getFieldName().equals("blockColor")) {
                        f.replace(String.format("{ $_ = %1$s.renderBurningBlock(this, $proceed()); }",
                                RenderBurningBlockPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static void renderFireOnBlock(SpriteBatch sb, float x, float y, float BLOCK_ICON_X, float BLOCK_ICON_Y, float blockOffset) {
        sb.draw(BURNING_TEXTURE, x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 28.0F + blockOffset, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F);
    }

    public static Color renderBurningBlock(AbstractCreature creature, Color blockColor) {
        if (creature != null && creature.hasPower(BurningPower.POWER_ID)) {
            float lerpCoefficient = ((float) creature.getPower(BurningPower.POWER_ID).amount) / creature.currentBlock * 0.3F + 0.1F;
            return blockColor.cpy().lerp(Color.RED.cpy(), lerpCoefficient);
        } else {
            return blockColor;
        }
    }
}
