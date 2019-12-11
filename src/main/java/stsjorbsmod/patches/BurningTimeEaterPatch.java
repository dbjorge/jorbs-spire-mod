package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.vfx.CloudBubble;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.BurningPower;

import java.util.Iterator;

public class BurningTimeEaterPatch {
    private static final String UI_ID = JorbsMod.makeID(BurningTimeEaterPatch.class);
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(UI_ID).TEXT;

    @SpirePatch(clz = RemoveDebuffsAction.class, method = "update")
    public static class RemoveDebuffsAction_Update_BurningTimeEater {
        public static ExprEditor Instrument() {
            return new ExprEditor() {

                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(Iterator.class.getName())
                            && methodCall.getMethodName().equals("next")) {
                        methodCall.replace("{ $_ = $proceed(); stsjorbsmod.patches.BurningTimeEaterPatch.burningTimeEater(c, $_); }");
                    }
                }
            };
        }
    }

    public static void burningTimeEater(AbstractCreature c, Object p) {
        if (c instanceof TimeEater && p instanceof BurningPower) {
            // This should happen after burning gets removed, but before the healing action.
            AbstractDungeon.actionManager.actions.forEach(a -> updateHealing(a, c));
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, TEXT[0], true));
        }
    }

    private static void updateHealing(AbstractGameAction action, AbstractCreature c) {
        if (action instanceof HealAction && action.target == c && action.source == c) {
            action.amount = 0;
        }
    }
}
