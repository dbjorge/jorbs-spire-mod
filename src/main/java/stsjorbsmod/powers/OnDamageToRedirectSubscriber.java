package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface OnDamageToRedirectSubscriber {
    boolean onDamageToRedirect(AbstractPlayer player, DamageInfo info, AttackEffect effect);
}
