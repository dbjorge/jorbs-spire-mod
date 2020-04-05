package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class SelfExertField {
    @SuppressWarnings("unchecked")
    public static SpireField<Boolean> selfExert = new SelfExertSpireField(() -> false);

    private static class SelfExertSpireField extends SpireField<Boolean> {
        SelfExertSpireField(DefaultValue<Boolean> defaultValue) {
            super(defaultValue);
        }

        public void set(Object __instance, Boolean value) {
            super.set(__instance, value);
            if (value && __instance instanceof AbstractCard) {
                ((AbstractCard)__instance).exhaust = true;
            }
        }
    }
}