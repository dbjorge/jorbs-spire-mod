package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.localization.RelicStrings;

@SpirePatch(clz = RelicStrings.class, method=SpirePatch.CLASS)
public class RelicStringsFieldPatch {
    public static SpireField<String[]> STAT_DESCRIPTIONS = new SpireField<>(() -> new String[0]);
}
