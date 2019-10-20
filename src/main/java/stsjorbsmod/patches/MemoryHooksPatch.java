package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.IOnModifyGoldListener;

import java.util.ArrayList;
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
        @SpireInsertPatch(
                locator = Locator.class
        )
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
}