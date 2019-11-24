package stsjorbsmod.cards;

public interface OnPlayerHpLossCardSubscriber {
    int onPlayerHpLossWhileInHand(int originalHpLoss);
}
