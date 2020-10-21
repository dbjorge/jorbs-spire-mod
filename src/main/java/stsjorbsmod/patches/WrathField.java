package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class WrathField {
    public static SpireField<Integer> wrathEffectCount = new SpireField<>(() -> 0);

    public static boolean usesMiscToTrackPermanentBaseDamage(AbstractCard c) {
        return c instanceof RitualDagger;
    }

    public static void updateCardDamage(AbstractCard instance, int damageChange) {
        instance.baseDamage += damageChange;
        if (usesMiscToTrackPermanentBaseDamage(instance)) {
            instance.misc += damageChange;
        }
    }
}
