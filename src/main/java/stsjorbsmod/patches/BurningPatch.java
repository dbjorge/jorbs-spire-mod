package stsjorbsmod.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import stsjorbsmod.powers.BurningPower;
import stsjorbsmod.util.ReflectionUtils;

public class BurningPatch {
    @SpirePatch(clz = EndTurnAction.class, method = "update")
    public static class AbstractRoom_endTurn {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractDungeon.class.getName()) && f.getFieldName().equals("topLevelEffects")) {
                        f.replace(String.format("{ %1$s.performTurnStartBurningCheck(); $_ = $proceed(); }",
                                BurningPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static void performTurnStartBurningCheck() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.hasPower(InvinciblePower.POWER_ID)) {
                m.getPower(InvinciblePower.POWER_ID).atStartOfTurn();
            }
            if (m.hasPower(BurningPower.POWER_ID)) {
                m.getPower(BurningPower.POWER_ID).onSpecificTrigger();
            }
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "applyStartOfTurnPowers")
    public static class AbstractCreature_applyStartOfTurnPowers {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().contains(AbstractPower.class.getName()) && m.getMethodName().equals("atStartOfTurn")) {
                        m.replace(String.format("{ if (!%1$s.isInvinciblePower($0)) { $_ = $proceed(); }}",
                                BurningPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static boolean isInvinciblePower(AbstractPower p) {
        return p.ID.equals(InvinciblePower.POWER_ID);
    }

    static TextureAtlas.AtlasRegion BURNING_TEXTURE = new TextureAtlas(Gdx.files.internal("powers/powers.atlas")).findRegion("128/attackBurn");
    static float ROTATION_DURATION = 60.0F;

    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    public static class AbstractCreatureBurningFields {
        public static SpireField<Float> burningRotation = new SpireField<>(() -> 0.0F);
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
    public static class AbstractCreature_renderBlockIconAndValue_showFire {
        @SpirePrefixPatch
        public static void patch(AbstractCreature __this, SpriteBatch sb, float x, float y) {
            if (__this.hasPower(BurningPower.POWER_ID)) {
                sb.setColor(Color.WHITE.cpy());
                float burningRotation = AbstractCreatureBurningFields.burningRotation.get(__this) / ROTATION_DURATION * 360.0F;
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
            }
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractCreature.class.getName()) && f.getFieldName().equals("blockColor")) {
                        f.replace(String.format("{ $_ = %1$s.renderBurningBlock(this, $proceed()); }",
                                BurningPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static Color renderBurningBlock(AbstractCreature creature, Color blockColor) {
        if (creature != null && creature.hasPower(BurningPower.POWER_ID)) {
            float lerpCoefficient = ((float) creature.getPower(BurningPower.POWER_ID).amount) / creature.currentBlock * 0.3F + 0.1F;
            return blockColor.cpy().lerp(Color.RED.cpy(), lerpCoefficient);
        } else {
            return blockColor;
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "updateAnimations")
    public static class AbstractCreature_updateBurningRotationTimer {
        @SpirePrefixPatch
        public static void patch(AbstractCreature __this) {
            if (__this.hasPower(BurningPower.POWER_ID)) {
                AbstractCreatureBurningFields.burningRotation.set(__this, AbstractCreatureBurningFields.burningRotation.get(__this) - Gdx.graphics.getDeltaTime());
                if (AbstractCreatureBurningFields.burningRotation.get(__this) < 0.0F) {
                    AbstractCreatureBurningFields.burningRotation.set(__this, ROTATION_DURATION);
                }
            }
        }
    }
}
