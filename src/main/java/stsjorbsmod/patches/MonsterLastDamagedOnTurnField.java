package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = SpirePatch.CLASS
)
public class MonsterLastDamagedOnTurnField {
    @SuppressWarnings("unchecked")
    public static SpireField<Integer> lastDamagedOnTurn = new SpireField(() -> -1);
}