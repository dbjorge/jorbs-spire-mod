package stsjorbsmod.memories;

// For use by powers/relics/memories; implement this and the MemoryManager will call it appropriately.
public interface OnModifyMemoriesSubscriber {
    default void onRememberMemory(String memoryID) { }
    default void onGainClarity(String memoryID) { }
    default void onLoseClarity(String memoryID) { }
    default void onForgetMemory() { }
    default void onSnap() { }

    default boolean onRememberMemoryToCancel(String memoryIDBeingRemembered) {
        return false;
    }
}
