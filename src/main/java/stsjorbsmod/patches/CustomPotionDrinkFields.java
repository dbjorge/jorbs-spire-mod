package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.potions.AbstractPotion;

@SpirePatch(clz = AbstractPotion.class, method = SpirePatch.CLASS)
public class CustomPotionDrinkFields {
    // The text to show in place of the "Drink" or "Throw" when clicking the potion
    public static SpireField<String> customDrinkText = new SpireField<>(() -> null);
    // The sound to play when "drinking" the potion
    public static SpireField<String> customDrinkSound = new SpireField<>(() -> null);
    // The sound to play when hovering the potion in the main game top bar UI
    public static SpireField<String> customHoverSound = new SpireField<>(() -> null);
}
