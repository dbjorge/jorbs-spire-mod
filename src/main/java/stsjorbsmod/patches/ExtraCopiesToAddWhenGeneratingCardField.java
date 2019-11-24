package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

// This is used for Rot's "When then is added to hand add one copy of it to hand."
@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class ExtraCopiesToAddWhenGeneratingCardField {
    @SuppressWarnings("unchecked")
    public static SpireField<Integer> field = new SpireField(() -> 0);
}