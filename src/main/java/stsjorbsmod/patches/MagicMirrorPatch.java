package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.IntentUtils;

// This addresses issue #171, where some base game debuffs assume they'll only ever be applied to the player and have
// unexpected interactions when mirrored with Magic Mirror
public class MagicMirrorPatch {
    private static final String UI_ID = JorbsMod.makeID(MagicMirrorPatch.class);
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(UI_ID).TEXT;

    public static boolean entangledAndAttacking(AbstractMonster m) {
        boolean retval = m.hasPower(EntanglePower.POWER_ID) && IntentUtils.isAttackIntent(m.intent);
        if (retval) {
            AbstractDungeon.effectList.add(new TextAboveCreatureEffect(m.hb.cX - m.animX, m.hb.cY + m.hb.height / 2.0F, TEXT[0], Color.WHITE.cpy()));
        }
        return retval;
    }

    // Cause EntanglePower to stun enemies that choose to attack after it's been applied
    // Modeled after StSLib's StunMonsterPatch (be careful to not break interactions with it!)
    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class GameActionManager_getNextAction {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractMonster.class.getName()) && m.getMethodName().equals("takeTurn")) {
                        m.replace(String.format("if (!%1$s.entangledAndAttacking($0)) {$_ = $proceed($$);}", MagicMirrorPatch.class.getName()));
                    }
                }
            };
        }
    }

    // Fixes EntanglePower only normally expiring at the end of player turns (which doesn't trigger for monsters)
    @SpirePatch(
            clz = EntanglePower.class,
            method = "atEndOfTurn"
    )
    public static class EntanglePower_atEndOfTurn {
        @SpirePrefixPatch
        public static void prefix(EntanglePower __this, @ByRef boolean[] isPlayer) {
            if (!__this.owner.isPlayer && !EntanglePowerJustAppliedField.justApplied.get(__this)) {
                // We intentionally use reduce rather than remove (like the original method) because it's possible for
                // this to end up stacked (if the player had multiple magic mirror stacks)
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(__this.owner, __this.owner, EntanglePower.POWER_ID, 1));
            }
            EntanglePowerJustAppliedField.justApplied.set(__this, false);
        }
    }

    @SpirePatch(
            clz = EntanglePower.class,
            method = SpirePatch.CLASS
    )
    public static class EntanglePowerJustAppliedField {
        public static SpireField<Boolean> justApplied = new SpireField<>(() -> true);
    }
}