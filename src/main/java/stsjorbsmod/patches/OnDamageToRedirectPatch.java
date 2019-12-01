package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.OnDamageToRedirectSubscriber;

public class OnDamageToRedirectPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class AbstractPlayer_damage {
        @SpirePrefixPatch
        public static SpireReturn patch(AbstractPlayer __this, DamageInfo info) {
            for (AbstractPower p : __this.powers) {
                if (p instanceof OnDamageToRedirectSubscriber) {
                    if (((OnDamageToRedirectSubscriber)p).onDamageToRedirect(__this, info)) {
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
