package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

public class EphemeralPatch {
    public static void exhaustVisual(AbstractCard c) {
        AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(c));
    }

    public static boolean isEphemeral(AbstractCard c) {
        return EphemeralField.ephemeral.get(c);
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    public static class ShowCardAndAddToDiscardEffect_ctor {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().contains(AbstractPlayer.class.getName()) && f.getFieldName().equals("discardPile")) {
                        f.replace(String.format("{" +
                                        "$_ = (%1$s.isEphemeral(this.card) ? $0.exhaustPile : $proceed());" +
                                        "}",
                                EphemeralPatch.class.getName()));
                    }
                }
            };
        }
    }

    @SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
    public static class ShowCardAndAddToDiscardEffect_update {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().contains(SoulGroup.class.getName()) && m.getMethodName().equals("discard")) {
                        m.replace(String.format("{" +
                                        "if (%1$s.isEphemeral(this.card)) { %1$s.exhaustVisual($1); }" +
                                        "else { $_ = $proceed($$); }" +
                                        "}",
                                EphemeralPatch.class.getName()));
                    }
                }
            };
        }
    }
}

