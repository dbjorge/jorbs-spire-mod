package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = SpirePatch.CLASS
)
public class SelfExhumeFields {
    @SuppressWarnings("unchecked")
    public static SpireField<Boolean> selfExhumeOnSnap = new SpireField(() -> false);
    @SuppressWarnings("unchecked")
    public static SpireField<Boolean> selfExhumeOnKill = new SpireField(() -> false);
    public static SpireField<Boolean> selfExhumeOnTurnX = new SpireField(() -> false);
}