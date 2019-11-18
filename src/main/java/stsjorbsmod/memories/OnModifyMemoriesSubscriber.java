package stsjorbsmod.memories;

import java.util.Set;

// For use by powers; implement this and the MemoryManager will call it appropriately.
public interface OnModifyMemoriesSubscriber {
    void onModifyMemories();

    MemoryManager.MemoryEventType[] getMemoryEventTypes();
}
