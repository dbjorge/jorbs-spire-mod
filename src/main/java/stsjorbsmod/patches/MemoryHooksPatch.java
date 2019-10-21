package stsjorbsmod.patches;

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
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

import java.util.Collections;
import java.util.function.Consumer;

public class MemoryHooksPatch {
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

    // Directly locating the relevant atDamageGive calls to insert next to would be very fragile
    // because of how the implementation is laid out, so instead, we patch before and after to add in
    // a fake power to receive and redirect the atDamageGive effect
    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class atDamageGiveHooks {
        private final static AbstractPower hookPower = new AbstractPower() {
            @Override
            public float atDamageGive(float damage, DamageInfo.DamageType type) {
                MemoryManager memoryManager = MemoryManager.forPlayer(AbstractDungeon.player);
                if (memoryManager != null) {
                    for (AbstractMemory memory : memoryManager.currentMemories()) {
                        damage = memory.atDamageGive(damage, type);
                    }
                }
                return damage;
            }
        };
        @SpirePrefixPatch
        public static void prefixPatch(AbstractCard __this) {
            AbstractDungeon.player.powers.add(hookPower);
        }

        @SpirePostfixPatch
        public static void postfixPatch(AbstractCard __this) {
            AbstractDungeon.player.powers.remove(hookPower);
        }
    }

    // Directly locating the relevant atDamageGive calls to insert next to would be very fragile
    // because of how the implementation is laid out, so instead, we patch before and after to add in
    // a fake power to receive and redirect the atDamageGive effect
    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class onAttackHooks {
        private final static AbstractPower hookPower = new AbstractPower() {
            @Override
            public void onAttack(DamageInfo info, int damage, AbstractCreature target) {
                if (target.isPlayer || target.isDead || target.isDying || target.halfDead || target.hasPower(MinionPower.POWER_ID)) {
                    return;
                }

                if (damage >= target.currentHealth) {
                    forEachMemory(info.owner, m -> m.onNonMinionMonsterDeath());
                }
            }
        };

        @SpirePrefixPatch
        public static void prefixPatch(AbstractMonster __this) {
            AbstractDungeon.player.powers.add(hookPower);
        }

        @SpirePostfixPatch
        public static void postfixPatch(AbstractMonster __this) {
            AbstractDungeon.player.powers.remove(hookPower);
        }
    }
}