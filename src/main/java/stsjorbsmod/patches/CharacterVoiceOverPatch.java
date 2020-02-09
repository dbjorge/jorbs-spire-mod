package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import com.megacrit.cardcrawl.screens.DeathScreen;
import stsjorbsmod.characters.CharacterVoiceOver;
import stsjorbsmod.effects.EmphasizedSFXEffect;
import stsjorbsmod.powers.BanishedPower;

public class CharacterVoiceOverPatch {
    @SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR)
    public static class DeathScreen_ctor {
        @SpirePostfixPatch
        public static void Postfix(DeathScreen __this) {
            if (!CharacterVoiceOver.isMuted()) {
                AbstractDungeon.effectsQueue.add(new EmphasizedSFXEffect(
                        () -> CharacterVoiceOver.play(__this.isVictory ? "death_victory" : "death"),
                        1.5F));
            }
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "playSfx")
    public static class NeowEvent_playSfx {
        @SpirePrefixPatch
        public static void Prefix(NeowEvent __this) {
            CharacterVoiceOver.play("new_run");
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class AbstractPlayer_preBattlePrep {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __this) {
            String voiceoverKey = CharacterVoiceOver.keyForCurrBattle();
            if (voiceoverKey != null && !CharacterVoiceOver.isMuted()) {
                AbstractDungeon.effectsQueue.add(new EmphasizedSFXEffect(() -> CharacterVoiceOver.play(voiceoverKey)));
            }
        }
    }
}




