package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
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
            if (m.hasPower(BurningPower.POWER_ID)) {
                m.getPower(BurningPower.POWER_ID).onSpecificTrigger();
            }
        }
    }


    @SpirePatch(clz = AbstractCreature.class, method = "renderBlockIconAndValue")
    public static class AbstractCreature_renderBlockIconAndValue {
        @SpirePrefixPatch

        public static void patch(AbstractCreature __this, SpriteBatch sb, float x, float y) {
            if (__this.hasPower(BurningPower.POWER_ID)) {
                sb.setColor(Color.GOLD.cpy());
                sb.draw(ImageMaster.ATK_FIRE,
                        x + (Float) ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "BLOCK_ICON_X") - 32.0F,
                        y + (Float) ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "BLOCK_ICON_Y") - 32.0F + (Float) ReflectionUtils.getPrivateField(__this, AbstractCreature.class, "blockOffset"),
                        32.0F,
                        32.0F,
                        64.0F,
                        64.0F,
                        Settings.scale,
                        Settings.scale,
                        0.0F);
            }
        }
    }
}
