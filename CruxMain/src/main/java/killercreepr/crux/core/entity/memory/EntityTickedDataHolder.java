package killercreepr.crux.core.entity.memory;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EntityTickedDataHolder extends EntityDataHolder implements TickedDataHolder {
    public EntityTickedDataHolder(@NotNull Key key, @NotNull EntityMemory parent) {
        super(key, parent);
    }

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Entity e) {
        return false;
    }

    @Override
    public void removing(@Nullable Entity e) {

    }
}
