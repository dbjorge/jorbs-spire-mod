package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.runHistory.TinyCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.Wanderer;

public class TinyCardColorPatch {
    @SpirePatch(clz = TinyCard.class, method = "getIconBackgroundColor")
    public static class BackgroundColorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(TinyCard __this, AbstractCard card) {
            if (card.color == Wanderer.Enums.WANDERER_CARD_COLOR) {
                return SpireReturn.Return(Wanderer.ColorInfo.CHARACTER_COLOR);
            } else if (card.color == Cull.Enums.CULL_CARD_COLOR) {
                return SpireReturn.Return(Cull.ColorInfo.CHARACTER_COLOR);
            } /* TODO: uncomment me: else if (card.color == Explorer.Enums.EXPLORER_CARD_COLOR) {
                return SpireReturn.Return(Explorer.ColorInfo.CHARACTER_COLOR);
            } */ else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = TinyCard.class, method = "getIconDescriptionColor")
    public static class DescriptionColorPatch {
        @SpirePrefixPatch
        public static SpireReturn<Color> Prefix(TinyCard __this, AbstractCard card) {
            if (card.color == Wanderer.Enums.WANDERER_CARD_COLOR) {
                return SpireReturn.Return(Color.GRAY);
            } else if (card.color == Cull.Enums.CULL_CARD_COLOR) {
                return SpireReturn.Return(Color.GRAY);
            } /* TODO: uncomment me: else if (card.color == Explorer.Enums.EXPLORER_CARD_COLOR) {
                return SpireReturn.Return(Color.GRAY);
            } */ else {
                return SpireReturn.Continue();
            }
        }
    }
}
