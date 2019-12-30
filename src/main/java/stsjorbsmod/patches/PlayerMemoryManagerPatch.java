package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

import java.util.Collections;
import java.util.function.Consumer;

public class PlayerMemoryManagerPatch {
    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CLASS)
    public static class MemoryManagerField {
        public static SpireField<MemoryManager> memoryManager = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CONSTRUCTOR)
    public static class AbstractPlayer_CONSTRUCTOR {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this) {
            MemoryManagerField.memoryManager.set(__this, new MemoryManager(__this));
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "render")
    public static class AbstractPlayer_renderHealth {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this, SpriteBatch sb) {
            if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !__this.isDead) {
                MemoryManagerField.memoryManager.get(__this).render(sb);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "update")
    public static class AbstractPlayer_updatePowers {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this) {
            MemoryManagerField.memoryManager.get(__this).update(__this.drawX, __this.drawY);
        }
    }
}