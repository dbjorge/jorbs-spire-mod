package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.cards.EntombedBehavior;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class EntombedField {
    @SuppressWarnings("unchecked")
    public static SpireField<EntombedBehavior> entombedBehavior = new SpireField(() -> null);
}