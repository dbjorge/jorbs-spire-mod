package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.OnDamagedPreBlockSubscriber;

import java.util.ArrayList;

public class OnDamagedPreBlockPatch {
    public static void invokeSubscribers(ArrayList<AbstractPower> candidatePowers, DamageInfo info) {
        for(AbstractPower power : candidatePowers) {
            if(power instanceof OnDamagedPreBlockSubscriber) {
                ((OnDamagedPreBlockSubscriber) power).onDamagedPreBlock(info);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractPlayer_damage
    {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __this, DamageInfo info)
        {
            invokeSubscribers(__this.powers, info);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class AbstractMonster_damage
    {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __this, DamageInfo info)
        {
            invokeSubscribers(__this.powers, info);
        }
    }
}