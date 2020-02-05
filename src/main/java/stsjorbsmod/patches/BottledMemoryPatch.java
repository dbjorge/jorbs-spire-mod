package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.relics.DollysMirror;
import javassist.CannotCompileException;
import javassist.CtBehavior;
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

    @SpirePatch(clz = Duplicator.class, method = "update")
    public static class Duplicator_update {
        @SpireInsertPatch(locator = AbstractCard_InBottleTornado_Locator.class, localvars = "c")
        public static void patch(Duplicator __this, AbstractCard c) {
            AbstractCardMemoryFields.inBottleMemory.set(c, false);
        }
    }

    @SpirePatch(clz = DollysMirror.class, method = "update")
    public static class DollysMirror_update {
        @SpireInsertPatch(locator = AbstractCard_InBottleTornado_Locator.class, localvars = "c")
        public static void patch(DollysMirror __this, AbstractCard c) {
            AbstractCardMemoryFields.inBottleMemory.set(c, false);
        }
    }

    public static class AbstractCard_InBottleTornado_Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "inBottleTornado");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }

    // The Fountain of Curse Removal event removes curse cards from the deck. It selects them directly from the
    // master deck. We don't let it remove any bottled curses.
    @SpirePatch(clz = FountainOfCurseRemoval.class, method = "buttonEffect")
    public static class FountainOfCurseRemoval_buttonEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    // replacing the "((AbstractCard)AbstractDungeon.player.masterDeck.group.get(i)).inBottleFlame"
                    // expression in the original if statement to add in an "|| card.hasTag(LEGENDARY)"
                    if (fieldAccess.getClassName().equals(AbstractCard.class.getName()) && fieldAccess.getFieldName().equals("inBottleFlame")) {
                        fieldAccess.replace("{ $_ = ($proceed() || stsjorbsmod.patches.BottledMemoryPatch.isInBottleMemory($0)); }");
                    }
                }
            };
        }
    }
}
