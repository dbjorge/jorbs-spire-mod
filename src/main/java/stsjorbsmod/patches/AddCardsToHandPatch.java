package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.powers.PathosPower;

@SpirePatch(
        clz = CardGroup.class,
        method = "addToHand"
)
public class AddCardsToHandPatch {

    @SpirePostfixPatch
    public static void Postfix(CardGroup __this, AbstractCard c) {
        if (AbstractDungeon.player.hasPower(PathosPower.POWER_ID)) {
            c.setCostForTurn(-9);
        }
    }
}




