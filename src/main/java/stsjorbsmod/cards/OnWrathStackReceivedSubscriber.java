package stsjorbsmod.cards;

// For use by cards. Implement this to add functionality when gaining a Wrath stack.
public interface OnWrathStackReceivedSubscriber {
    void onWrathStackReceived();

}
