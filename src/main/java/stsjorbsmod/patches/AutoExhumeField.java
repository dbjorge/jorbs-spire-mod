package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.cards.AutoExhumeBehavior;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class AutoExhumeField {
    @SuppressWarnings("unchecked")
    public static SpireField<AutoExhumeBehavior> autoExhumeBehavior = new SpireField(() -> null);
}