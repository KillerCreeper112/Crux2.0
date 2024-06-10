package killercreepr.crux.data.entity;

import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataHolder extends Keyed {
    boolean shouldRemoveFromMemory(@Nullable Entity e);
    void tick(@NotNull Entity e);

    /**
     * Called when the DataHolder is removed due to shouldRemovedFromMemory being true.
     */
    void removing(@Nullable Entity e);

    /**
     * Called when the parent (the object holding the DataHolder) is getting removed from memory.
     */
    void parentRemoving(@Nullable Entity e);
}
