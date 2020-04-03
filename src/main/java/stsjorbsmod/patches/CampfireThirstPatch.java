package stsjorbsmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.RestOption;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import stsjorbsmod.campfire.ThirstOption;
import stsjorbsmod.characters.Cull;

import java.util.Iterator;

@SpirePatch(
        clz = CampfireUI.class,
        method = "initializeButtons"
)
public class CampfireThirstPatch {
    public CampfireThirstPatch() {
    }

    public static ExprEditor Instrument() {
       return new ExprEditor() {
           public void edit(NewExpr e) throws CannotCompileException {
               // Completely replace the rest option with thirst for Cull
               if(e.getClassName().equals("com.megacrit.cardcrawl.ui.campfire.RestOption")) {
                    e.replace(String.format(
                            "{$_ = %1$s.getCullCampfireOption($proceed($$));}",
                            CampfireThirstPatch.class.getName()));
               }
           }
       };
    }

    public static CardGroup getWrathCards() {
        CardGroup masterDeck = AbstractDungeon.player.masterDeck;
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : masterDeck.group) {
            if (WrathField.wrathEffectCount.get(c) > 0) {
                retVal.group.add(c);
            }
        }

        return retVal;
    }

    public static AbstractCampfireOption getCullCampfireOption(AbstractCampfireOption originalOption) {
        if(AbstractDungeon.player instanceof Cull) {
            return new ThirstOption(getWrathCards().size() > 0);
        } else {
            return originalOption;
        }
    }
}
