package stsjorbsmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.CoffeeDripper;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import stsjorbsmod.campfire.ThirstOption;
import stsjorbsmod.characters.Cull;

@SpirePatch(clz = CoffeeDripper.class, method = "canUseCampfireOption")
public class CoffeeDripperThirstPatch {
    @SpirePrefixPatch
    public static SpireReturn<Boolean> prefix(CoffeeDripper __this, AbstractCampfireOption option) {
        if(AbstractDungeon.player instanceof Cull) {
            if (option instanceof ThirstOption && option.getClass().getName().equals(ThirstOption.class.getName())) {
                return SpireReturn.Return(false);
            }
        }
        return SpireReturn.Continue();
    }
}
