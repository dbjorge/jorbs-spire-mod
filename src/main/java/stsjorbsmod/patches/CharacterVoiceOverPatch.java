package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import com.megacrit.cardcrawl.screens.DeathScreen;
import stsjorbsmod.characters.CharacterVoiceOver;
import stsjorbsmod.powers.BanishedPower;

public class CharacterVoiceOverPatch {
    @SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR)
    public static class DeathScreen_ctor {
        @SpirePostfixPatch
        public static void Postfix(DeathScreen __this) {
            CharacterVoiceOver.play(__this.isVictory ? "death_victory" : "death");
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "playSfx")
    public static class NeowEvent_playSfx {
        @SpirePrefixPatch
        public static void Prefix(NeowEvent __this) {
            CharacterVoiceOver.play("new_run");
        }
    }
}




