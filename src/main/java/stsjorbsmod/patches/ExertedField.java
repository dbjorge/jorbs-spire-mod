package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class ExertedField {
    public static SpireField<Boolean> exerted = new SpireField<>(() -> false);
    public static SpireField<Boolean> exertedAtStartOfCombat = new SpireField<>(() -> false);
}