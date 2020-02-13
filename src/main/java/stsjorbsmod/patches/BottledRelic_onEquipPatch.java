package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;
import static stsjorbsmod.patches.EntombedPatch.isEntombed;

@SpirePatch(clz = BottledFlame.class, method = "onEquip")
@SpirePatch(clz = BottledLightning.class, method = "onEquip")
@SpirePatch(clz = BottledTornado.class, method = "onEquip")
public class BottledRelic_onEquipPatch {
    /**
     * Returns all the bottleable cards in the parameter CardGroup. Since CardGroup::getPurgeableCards is patched to
     * never return Legendary cards, we add them back into this CardGroup because they can be bottled.
     *
     * @param purgeableCards Likely the result of masterdeck.getPurgeableCards
     * @return all bottleable cards from the CardGroup passed in.
     */
    public static CardGroup getCardsForBottling(CardGroup purgeableCards) {
        ArrayList<AbstractCard> originalCardList = new ArrayList<>(purgeableCards.group);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.hasTag(LEGENDARY)) {
                originalCardList.add(c);
            }
        }
        CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : purgeableCards.group) {
            if (!isEntombed(c)) {
                result.group.add(c);
            }
        }
        return result;
    }

    public static class GetBottleableCardsExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(CardGroup.class.getName()) && methodCall.getMethodName().equals("getPurgeableCards")) {
                methodCall.replace(String.format("{ $_ = %1$s.getCardsForBottling($proceed($$)); }", BottledRelic_onEquipPatch.class.getName()));
            }
        }
    }

    public static ExprEditor Instrument() {
        return new GetBottleableCardsExprEditor();
    }
}
