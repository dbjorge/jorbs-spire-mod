package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.cards.AutoExhumeBehavior;

import java.util.EnumSet;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class SelfExhumeFields {
    @SuppressWarnings("unchecked")
    public static SpireField<Boolean> selfExhumeOnSnap = new SpireField(() -> false);
    @SuppressWarnings("unchecked")
    public static SpireField<Boolean> selfExhumeAtStartOfTurn7 = new SpireField(() -> false);
    @SuppressWarnings("unchecked")
    public static SpireField<Boolean> selfExhumeOnKill = new SpireField(() -> false);
}