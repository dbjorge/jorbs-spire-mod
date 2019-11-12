package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.wanderer.materialcomponents.MaterialComponentsDeck;

public class MaterialComponentsDeckPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class ResetAtStartOfCombatHook {
        @SpirePrefixPatch
        public static void prefixPatch(AbstractPlayer __this) {
            MaterialComponentsDeck.reset();
        }
    }

    // Note: this is strictly a sentinel value for use with DiscoverAction; individual material component cards
    // should not use this (they should use CardRarity.SPECIAL and the card type appropriate for their effect)
    @SpireEnum
    public static AbstractCard.CardType MATERIAL_COMPONENT;

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "returnTrulyRandomCardInCombat",
            paramtypez = { AbstractCard.CardType.class }
    )
    public static class AbstractDungeon_returnTrulyRandomCardInCombat {
        @SpirePrefixPatch
        public static SpireReturn<AbstractCard> patch(AbstractCard.CardType cardType) {
            if (cardType == MATERIAL_COMPONENT) {
                return SpireReturn.Return(MaterialComponentsDeck.drawRandomCard());
            }

            return SpireReturn.Continue();
        }
    }
}