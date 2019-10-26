package stsjorbsmod.patches;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.DamageHooks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MemoryHooksPatch {
    public static final Logger logger = LogManager.getLogger(MemoryHooksPatch.class.getName());

    private static void forEachMemory(AbstractCreature owner, Consumer<AbstractMemory> callback) {
        MemoryManager memoryManager = MemoryManager.forPlayer(owner);
        if (memoryManager != null) {
            for (AbstractMemory memory : memoryManager.currentMemories()) {
                callback.accept(memory);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class onPlayCardHook {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(GameActionManager __this) {
            forEachMemory(AbstractDungeon.player, m -> m.onPlayCard(__this.cardQueue.get(0).card, __this.cardQueue.get(0).monster));
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "blights");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "applyEndOfTurnTriggers"
    )
    public static class atEndOfTurnHook {
        @SpirePostfixPatch
        public static void patch(AbstractCreature __this) {
            forEachMemory(__this, m -> m.atEndOfTurn(__this.isPlayer));
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class onVictoryHook {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __this) {
            if (!__this.isDying) {
                forEachMemory(__this, m -> m.onVictory());
            }
        }
    }

    // This hook is based on basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.DamageHooks.ApplyPowers
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class atDamageGiveHook_ApplyPowersSingle {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}
        )
        public static void Insert(AbstractCard __this, @ByRef float[] tmp) {
            AbstractPlayer player = AbstractDungeon.player;
            MemoryManager memoryManager = MemoryManager.forPlayer(player);
            if (memoryManager != null) {
                for (AbstractMemory memory : memoryManager.currentMemories()) {
                    tmp[0] = memory.atDamageGive(tmp[0], __this.damageTypeForTurn);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher matcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctBehavior, matcher);
            }
        }
    }

    // This hook is based on basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.DamageHooks.ApplyPowersMulti
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class atDamageGiveHook_ApplyPowersMulti {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp", "i"}
        )
        public static void Insert(AbstractCard __this, float[] tmp, int i) {
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;
            AbstractPlayer player = AbstractDungeon.player;
            MemoryManager memoryManager = MemoryManager.forPlayer(player);
            if (memoryManager != null) {
                for (AbstractMemory memory : memoryManager.currentMemories()) {
                    tmp[i] = memory.atDamageGive(tmp[i], __this.damageTypeForTurn);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher intermediateMatcher = new Matcher.FieldAccessMatcher(AbstractRoom.class, "monsters");
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctBehavior, Collections.singletonList(intermediateMatcher), finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class onMonsterDeathHook {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = "info"
        )
        public static void patch(AbstractMonster __this, DamageInfo info) {
            AbstractPlayer player = AbstractDungeon.player;
            MemoryManager memoryManager = MemoryManager.forPlayer(player);
            if (memoryManager != null) {
                forEachMemory(player, m -> m.onMonsterDeath(__this, info));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "die");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class ClearAtStartOfCombatHook {
        @SpirePrefixPatch
        public static void prefixPatch(AbstractPlayer __this) {
            MemoryManager.forPlayer(__this).clear();
        }
    }
}