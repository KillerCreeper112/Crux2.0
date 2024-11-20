package killercreepr.crux.api.entity.memory;

import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface DataHolder extends Keyed {
    /**
     * Called when the parent (the object holding the DataHolder) is getting removed from memory.
     */
    void parentRemoving(@Nullable Entity e);
}
