package stsjorbsmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface OnDamageToRedirectSubscriber {
    boolean onDamageToRedirect(AbstractPlayer player, DamageInfo info);
}
