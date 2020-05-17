package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.actions.DecreaseMaxHpAction;
import stsjorbsmod.powers.SacrificePower;

public class SacrificeEnergyPatch {

    @SpirePatch(clz = EnergyManager.class, method = "use")
    public static class SacrificeEnergyPatch_UseEnergy {
        private static AbstractPlayer p = AbstractDungeon.player;

        @SpirePrefixPatch
        public static void Insert(EnergyManager __this, int e) {
            if (p != null && p.hasPower(SacrificePower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(
                        new DecreaseMaxHpAction(p, p, e, AbstractGameAction.AttackEffect.POISON));
                SpireReturn.Return(null);
            }
        }
    }
}