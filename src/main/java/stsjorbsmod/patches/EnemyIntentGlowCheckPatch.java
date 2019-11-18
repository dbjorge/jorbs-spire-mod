package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

// This is for the benefit of cards like Thorns, which use gold-glow checks based on enemy intents
public class EnemyIntentGlowCheckPatch {
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "createIntent"
    )
    public static class AbstractMonster_createIntent
    {
        @SpirePostfixPatch
        public static void patch(AbstractMonster __this)
        {
            AbstractDungeon.player.hand.glowCheck();
        }
    }
}