package killercreepr.cruxentities.wrapper;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityWrapper implements IEntityWrapper {
    protected final @NotNull Entity entity;
    public EntityWrapper(@NotNull Entity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Entity entity() {
        return entity;
    }
}
