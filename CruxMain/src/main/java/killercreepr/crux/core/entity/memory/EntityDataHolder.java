package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.entity.memory.DataHolder;
import killercreepr.crux.api.entity.memory.EntityMemory;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EntityDataHolder implements DataHolder {
    protected final Key key;
    protected final EntityMemory parent;
    public EntityDataHolder(@NotNull Key key, @NotNull EntityMemory parent) {
        this.key = key;
        this.parent = parent;
    }

    protected void removingFromMemory(@Nullable Entity e){}

    @Override
    public void parentRemoving(@Nullable Entity e) {
        removingFromMemory(e);
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public @NotNull EntityMemory getParent() {
        return parent;
    }
}
