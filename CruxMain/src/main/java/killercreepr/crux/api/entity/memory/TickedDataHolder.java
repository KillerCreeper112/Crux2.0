package killercreepr.crux.api.entity.memory;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TickedDataHolder extends DataHolder {
    boolean shouldRemoveFromMemory(@Nullable Entity e);

    /**
     * Called when the DataHolder is removed due to shouldRemovedFromMemory being true.
     */
    void removing(@Nullable Entity e);
    void tick(@NotNull Entity e);
}
