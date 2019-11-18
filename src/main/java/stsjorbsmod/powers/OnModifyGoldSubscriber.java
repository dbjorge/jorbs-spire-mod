package stsjorbsmod.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface OnModifyGoldSubscriber {
    void onModifyGold(AbstractPlayer player);
}
