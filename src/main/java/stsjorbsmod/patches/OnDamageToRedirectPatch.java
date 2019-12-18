package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.OnDamageToRedirectSubscriber;
import stsjorbsmod.util.ReflectionUtils;

public class OnDamageToRedirectPatch {
    // This is the normal case
    @SpirePatch(
            clz = DamageAction.class,
            method = "update"
    )
    public static class DamageAction_update {
        @SpirePrefixPatch
        public static SpireReturn patch(DamageAction __this) {
            if (!__this.target.isPlayer) {
                return SpireReturn.Continue();
            }

            AbstractPlayer target = (AbstractPlayer) __this.target;
            DamageInfo info = ReflectionUtils.getPrivateField(__this, DamageAction.class, "info");

            for (AbstractPower p : target.powers) {
                if (p instanceof OnDamageToRedirectSubscriber) {
                    if (((OnDamageToRedirectSubscriber)p).onDamageToRedirect(target, info, __this.attackEffect)) {
                        __this.isDone = true;
                        return SpireReturn.Return(null);
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }

    private static final AttackEffect DEFAULT_ATTACK_EFFECT = AttackEffect.BLUNT_LIGHT;

    // This is the fallback case; this covers any custom actions that call player.damage() without going through
    // DamageAction. This strategy still works for redirecting the damage, but will usually result in missing
    // redirection of the attack effect.
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class AbstractPlayer_damage {
        @SpirePrefixPatch
        public static SpireReturn patch(AbstractPlayer __this, DamageInfo info) {
            for (AbstractPower p : __this.powers) {
                if (p instanceof OnDamageToRedirectSubscriber) {
                    if (((OnDamageToRedirectSubscriber)p).onDamageToRedirect(__this, info, DEFAULT_ATTACK_EFFECT)) {
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
