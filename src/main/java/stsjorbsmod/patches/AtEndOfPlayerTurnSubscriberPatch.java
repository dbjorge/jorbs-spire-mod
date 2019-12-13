package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.AtEndOfPlayerTurnSubscriber;

public class AtEndOfPlayerTurnSubscriberPatch {
    @SpirePatch(
            clz = AbstractCreature.class,
            method = "applyEndOfTurnTriggers"
    )
    public static class AbstractCreature_applyEndOfTurnTriggers
    {
        @SpirePostfixPatch
        public static void patch(AbstractCreature __this)
        {
            if (__this.isPlayer) {
                for (AbstractPower power : __this.powers) {
                    if (power instanceof AtEndOfPlayerTurnSubscriber) {
                        ((AtEndOfPlayerTurnSubscriber) power).atEndOfPlayerTurn();
                    }
                }

                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    for (AbstractPower power : monster.powers) {
                        if (power instanceof AtEndOfPlayerTurnSubscriber) {
                            ((AtEndOfPlayerTurnSubscriber) power).atEndOfPlayerTurn();
                        }
                    }
                }
            }
        }
    }
}