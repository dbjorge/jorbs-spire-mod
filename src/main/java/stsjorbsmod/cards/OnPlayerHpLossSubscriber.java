package stsjorbsmod.cards;

public interface OnPlayerHpLossSubscriber {
    int onPlayerHpLossWhileInHand(int originalHpLoss);
}
