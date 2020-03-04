package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
public class EphemeralField {
    public static SpireField<Boolean> ephemeral = new EphemeralSpireField(() -> false);

    private static class EphemeralSpireField extends SpireField<Boolean> {
        EphemeralSpireField(DefaultValue<Boolean> defaultValue) {
            super(defaultValue);
        }

        public void set(Object __instance, Boolean value) {
            super.set(__instance, value);
            if (value && __instance instanceof AbstractCard) {
                ((AbstractCard)__instance).exhaust = true;
                ((AbstractCard)__instance).isEthereal = true;
            }
        }
    }
}
