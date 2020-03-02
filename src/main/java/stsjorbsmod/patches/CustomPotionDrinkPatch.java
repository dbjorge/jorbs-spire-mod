package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.util.ReflectionUtils;

public class CustomPotionDrinkPatch {
    // Implements CustomPotionDrinkFields.customDrinkText
    @SpirePatch(clz = PotionPopUp.class, method = "render")
    public static class PotionPopUp_render {
        @SpireInsertPatch(locator = Locator.class, localvars = { "label" })
        public static void Insert(PotionPopUp __this, SpriteBatch sb, @ByRef String[] label) {
            AbstractPotion potion = ReflectionUtils.getPrivateField(__this, PotionPopUp.class, "potion");
            String customDrinkText = CustomPotionDrinkFields.customDrinkText.get(potion);
            if (customDrinkText != null) {
                label[0] = customDrinkText;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                final Matcher matcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCenteredWidth");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "obtainPotion", paramtypez = { AbstractPotion.class })
    public static class AbstractPlayer_obtainPotion {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractPotion.class.getName()) && methodCall.getMethodName().equals("playPotionSound")) {
                        methodCall.replace(String.format(
                                "{ if (! %1$s.playCustomPotionSound(potionToObtain)) { $proceed($$); } }",
                                CustomPotionDrinkPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static boolean playCustomPotionSound(AbstractPotion potion) {
        String customDrinkSound = CustomPotionDrinkFields.customDrinkSound.get(potion);
        if (customDrinkSound == null) {
            return false;
        }
        CardCrawlGame.sound.play(customDrinkSound);
        return true;
    }

    // Implements CustomPotionDrinkFields.customDrinkSound
    @SpirePatch(clz = PotionPopUp.class, method = "updateInput")
    public static class PotionPopUp_updateInput {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(SoundMaster.class.getName()) && methodCall.getMethodName().equals("play")) {
                        methodCall.replace(String.format(
                                "{ $_ = $proceed(%1$s.updateWithCustomDrinkSound($1, this.potion)); }",
                                CustomPotionDrinkPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static String updateWithCustomDrinkSound(String originalSoundId, AbstractPotion potion) {
        if (!originalSoundId.equals("POTION_1")) {
            return originalSoundId;
        }
        String customDrinkSound = CustomPotionDrinkFields.customDrinkSound.get(potion);
        return customDrinkSound != null ? customDrinkSound : originalSoundId;
    }

    // Implements CustomPotionDrinkFields.customHoverSound
    @SpirePatch(clz = TopPanel.class, method = "updatePotions")
    public static class TopPanel_updatePotions {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(SoundMaster.class.getName()) && methodCall.getMethodName().equals("play")) {
                        methodCall.replace(String.format(
                                "{ $_ = $proceed(%1$s.updateWithCustomHoverSound($1, p)); }",
                                CustomPotionDrinkPatch.class.getName()));
                    }
                }
            };
        }
    }

    public static String updateWithCustomHoverSound(String originalSoundId, AbstractPotion potion) {
        if (!(originalSoundId.equals("POTION_1") || originalSoundId.equals("POTION_3"))) {
            return originalSoundId;
        }
        String customHoverSound = CustomPotionDrinkFields.customHoverSound.get(potion);
        return customHoverSound != null ? customHoverSound : originalSoundId;
    }
}
