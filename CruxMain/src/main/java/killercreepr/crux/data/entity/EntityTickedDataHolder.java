package killercreepr.crux.data.entity;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EntityTickedDataHolder extends EntityDataHolder implements TickedDataHolder{
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
