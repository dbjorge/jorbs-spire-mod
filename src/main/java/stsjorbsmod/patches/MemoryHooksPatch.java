package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.SnakeDagger;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SplitPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.util.ReflectionUtils;

import java.util.Collections;
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
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class onAttackHook {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars = "damageAmount"
        )
        public static void patch(AbstractMonster __this, DamageInfo info, int damageAmount) {
            if (info.owner == AbstractDungeon.player) {
                forEachMemory(AbstractDungeon.player, m -> m.onAttack(info, damageAmount, __this));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher abstractPowerOnAttackedToChangeDamage = new Matcher.MethodCallMatcher(AbstractPower.class, "onAttackedToChangeDamage");
                Matcher abstractPlayerRelics = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
                return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(abstractPowerOnAttackedToChangeDamage), abstractPlayerRelics);
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
            clz = AbstractCreature.class,
            method = "applyStartOfTurnPostDrawPowers"
    )
    public static class atStartOfTurnPostDrawHook {
        @SpirePostfixPatch
        public static void patch(AbstractCreature __this) {
            forEachMemory(__this, m -> m.atStartOfTurnPostDraw());
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

    @SpirePatch(
            clz = AbstractMonster.class,
            method = SpirePatch.CLASS
    )
    public static class IsMonsterSuicidingField {
        @SuppressWarnings("unchecked")
        public static SpireField<Boolean> isSuiciding = new SpireField(() -> false);
    }

    @SpirePatch(
            clz = SuicideAction.class,
            method = "update"
    )
    public static class SuicideAction_trackIsMonsterSuiciding {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(SuicideAction __this) {
            AbstractMonster m = ReflectionUtils.getPrivateField(__this, SuicideAction.class, "m");
            IsMonsterSuicidingField.isSuiciding.set(m, true);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "die");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    // SnakeDagger is a special case where it suicides without using a SuicideAction
    @SpirePatch(
            clz = SnakeDagger.class,
            method = "takeTurn"
    )
    public static class SnakeDagger_trackIsMonsterSuiciding {
        @SpirePostfixPatch
        public static void patch(SnakeDagger __this) {
            if (__this.nextMove == 2) {
                IsMonsterSuicidingField.isSuiciding.set(__this, true);
            }
        }
    }

    // Note: it's very important this happen as a prefix to die() rather than as an insert before the die() call in
    // damage(); this is because isHalfDead gets set by subclasses (Darkling, AwakenedOne) overriding damage().
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "die",
            paramtypez = { boolean.class }
    )
    public static class onMonsterKilledHook {
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster __this) {
            // halfDead is for cases like darklings or awakened one; all "on monster death" memory effects want to ignore those cases.
            // isSuiciding is for effects like Transient/Exploder/SnakeDagger/slime splits (the player isn't "killing", so they don't count)
            if (!__this.halfDead && !IsMonsterSuicidingField.isSuiciding.get(__this)) {
                AbstractPlayer player = AbstractDungeon.player;
                MemoryManager memoryManager = MemoryManager.forPlayer(player);
                if (memoryManager != null) {
                    forEachMemory(player, m -> m.onMonsterKilled(__this));
                }
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
            MemoryManager memoryManager = MemoryManager.forPlayer(__this);
            if (memoryManager != null) {
                MemoryManager.forPlayer(__this).clear();
            }
        }
    }
}