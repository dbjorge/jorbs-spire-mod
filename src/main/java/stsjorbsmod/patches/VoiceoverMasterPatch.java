package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.audio.VoiceoverMaster;
import stsjorbsmod.audio.Voiceover;

public class VoiceoverMasterPatch {
    @SpirePatch(clz = MonsterGroup.class, method = SpirePatch.CLASS)
    public static class MonsterGroup_class {
        public static SpireField<String> encounterKeyField = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = MonsterHelper.class, method = "getEncounter")
    public static class MonsterHelper_getEncounter {
        @SpirePostfixPatch
        public static MonsterGroup Postfix(MonsterGroup __originalRetVal, String key) {
            MonsterGroup_class.encounterKeyField.set(__originalRetVal, key);
            return __originalRetVal;
        }
    }

    @SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR)
    public static class DeathScreen_ctor {
        @SpirePostfixPatch
        public static void Postfix(DeathScreen __this) {
            VoiceoverMaster.playAfterDelay(DeathScreen.isVictory ? "death_victory" : "death", 2.0F);
        }
    }

    @SpirePatch(clz = NeowEvent.class, method = "playSfx")
    public static class NeowEvent_playSfx {
        @SpirePrefixPatch
        public static void Prefix(NeowEvent __this) {
            VoiceoverMaster.play("new_run");
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class AbstractPlayer_preBattlePrep {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __this) {
            VoiceoverMaster.playForCurrentBattle();
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "update")
    public static class VoiceoverMasterUpdateHook {
        @SpirePrefixPatch
        public static void Prefix(CardCrawlGame __this) {
            VoiceoverMaster.update();
        }
    }

    @SpirePatch(clz = MainMusic.class, method="updateVolume")
    @SpirePatch(clz = MainMusic.class, method="updateFadeIn")
    @SpirePatch(clz = TempMusic.class, method="updateFadeIn")
    @SpirePatch(clz = TempMusic.class, method="updateFadeOut")
    @SpirePatch(clz = SoundMaster.class, method="update")
    @SpirePatch(clz = SoundMaster.class, method="play", paramtypez = {String.class, boolean.class})
    @SpirePatch(clz = SoundMaster.class, method="play", paramtypez = {String.class, float.class})
    @SpirePatch(clz = SoundMaster.class, method="playA")
    @SpirePatch(clz = SoundMaster.class, method="playV")
    @SpirePatch(clz = SoundMaster.class, method="playAV")
    @SpirePatch(clz = SoundMaster.class, method="playAndLoop", paramtypez = {String.class})
    @SpirePatch(clz = SoundMaster.class, method="adjustVolume", paramtypez = {String.class, long.class})
    public static class ModifyMasterVolumeByDampeningFactor {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(Settings.class.getName()) && fieldAccess.getFieldName().equals("MASTER_VOLUME")) {
                        fieldAccess.replace("{" +
                                "$_ = ($proceed() * " + VoiceoverMaster.class.getName() +".MASTER_DAMPENING_FACTOR);" +
                                "}");
                    }
                }
            };
        }
    }
}




