package killercreepr.crux.api.entity.memory;

import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataHolder extends Keyed {
    /**
     * Called when the parent (the object holding the DataHolder) is getting removed from memory.
     */
    void parentRemoving(@Nullable Entity e);
    default void scheduledRemoval(Entity e){}
    /**
     * This is called immediately when the EntityMemory that this is attached to is supposed to be
     * removed from memory (e.x., when a player quits the server or when an entity is removed from world)
     */
    default void onMemoryUnload(@NotNull Entity e){

    }
    default void adding(){}

    default boolean isLoaded(){ return true; }
    default void setLoaded(boolean loaded){}
    default boolean isDirty(){ return true; }
    default void setDirty(){}
}
