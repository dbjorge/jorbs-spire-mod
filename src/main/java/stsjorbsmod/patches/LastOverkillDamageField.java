package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = SpirePatch.CLASS
)
public class LastOverkillDamageField {
    @SuppressWarnings("unchecked")
    public static SpireField<Integer> lastOverkillDamage = new SpireField(() -> 0);
}
