package stsjorbsmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import stsjorbsmod.relics.BottledMemoryRelic;

public class BottledMemoryPatch {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class AbstractCardMemoryFields {
        public static SpireField<Boolean> inBottleMemory = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class AbstractCard_makeStatEquivalentCopy {
        @SpirePostfixPatch
        public static AbstractCard patch(AbstractCard __return, AbstractCard __this) {
            AbstractCardMemoryFields.inBottleMemory.set(__return, AbstractCardMemoryFields.inBottleMemory.get(__this));
            return __return;
        }
    }

    public static class BottledGetCardsExprEditor extends ExprEditor {
        private String methodName;

        BottledGetCardsExprEditor(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(CardGroup.class.getName()) && methodCall.getMethodName().equals(methodName)) {
                methodCall.replace("{ $_ = (stsjorbsmod.patches.BottledMemoryPatch.removeBottledCards($proceed())); }");
            }
        }
    }

    /**
     * Removes all bottled cards from the card group passed in and returns the CardGroup.
     *
     * @param cards
     * @return cards
     */
    public static CardGroup removeBottledCards(CardGroup cards) {
        cards.group.removeIf(c -> c.inBottleTornado || c.inBottleFlame || c.inBottleLightning || AbstractCardMemoryFields.inBottleMemory.get(c));
        return cards;
    }

    @SpirePatch(clz = BottledTornado.class, method = "onEquip")
    public static class BottledTornado_onEquip {
        public static ExprEditor Instrument() {
            return new BottledGetCardsExprEditor("getPowers");
        }
    }

    @SpirePatch(clz = BottledFlame.class, method = "onEquip")
    public static class BottledFlame_onEquip {
        public static ExprEditor Instrument() {
            return new BottledGetCardsExprEditor("getAttacks");
        }
    }

    @SpirePatch(clz = BottledLightning.class, method = "onEquip")
    public static class BottledLightning_onEquip {
        public static ExprEditor Instrument() {
            return new BottledGetCardsExprEditor("getSkills");
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "bottledCardUpgradeCheck", paramtypez = {AbstractCard.class})
    public static class AbstractPlayer_bottledCardUpgradeCheck {
        @SpirePostfixPatch
        public static void patch(AbstractPlayer __this, AbstractCard c) {
            if (AbstractCardMemoryFields.inBottleMemory.get(c) && __this.hasRelic(BottledMemoryRelic.ID)) {
                ((BottledMemoryRelic) __this.getRelic(BottledMemoryRelic.ID)).setDescriptionAfterLoading();
            }
        }
    }

    public static boolean isInBottleMemory(AbstractCard c) {
        return AbstractCardMemoryFields.inBottleMemory.get(c);
    }

    @SpirePatch(clz = CardGroup.class, method = "initializeDeck", paramtypez = {CardGroup.class})
    public static class AbstractGroup_initializeDeck {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    // Transforms `!inBottleTornado` into `!(inBottleTornado || inBottleMemory)`
                    // Logically equivalent to `!inBottleTornado && !inBottleMemory`
                    if (fieldAccess.getClassName().equals(AbstractCard.class.getName()) && fieldAccess.getFieldName().equals("inBottleTornado")) {
                        fieldAccess.replace("{ $_ = ($proceed() || stsjorbsmod.patches.BottledMemoryPatch.isInBottleMemory(c)); }");
                    }
                }
            };
        }
    }
}
