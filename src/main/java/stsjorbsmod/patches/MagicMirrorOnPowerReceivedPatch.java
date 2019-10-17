package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.powers.MagicMirrorPower;

import java.lang.reflect.Field;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = "update"
)
public class MagicMirrorOnPowerReceivedPatch {
    private static final Logger logger = LogManager.getLogger(MagicMirrorOnPowerReceivedPatch.class.getName());

    @SuppressWarnings("unchecked")
    private static <T> T getPrivateField(ApplyPowerAction __this, Class clz, String fieldName) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(__this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SpirePrefixPatch
    public static void patch(ApplyPowerAction __this) {
        logger.info("Entering patch");
        if (__this.target != null && !__this.target.isDeadOrEscaped()) {
            float duration = getPrivateField(__this, AbstractGameAction.class, "duration");
            float startingDuration = getPrivateField(__this, ApplyPowerAction.class, "startingDuration");
            if (duration == startingDuration) {
                AbstractPower powerToApply = getPrivateField(__this, ApplyPowerAction.class, "powerToApply");
                if (__this.target.hasPower(MagicMirrorPower.POWER_ID) && powerToApply.type == AbstractPower.PowerType.DEBUFF && __this.target != __this.source) {
                    logger.info("Magic Mirror triggering to reflect a power");
                    ((MagicMirrorPower)__this.target.getPower(MagicMirrorPower.POWER_ID)).onPowerReceived(powerToApply);
                }
            }
        }
        logger.info("Leaving patch");
    }
}
