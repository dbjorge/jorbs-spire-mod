package stsjorbsmod.memories;

import java.util.Set;

// For use by powers; implement this and the MemoryManager will call it appropriately.
public interface OnModifyMemoriesListener {
    void onModifyMemories();

    MemoryManager.MemoryEventType[] getMemoryEventTypes();
}
