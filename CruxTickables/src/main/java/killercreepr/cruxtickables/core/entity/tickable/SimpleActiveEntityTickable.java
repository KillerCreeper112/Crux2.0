package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxtickables.api.entity.tickable.ActiveEntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleActiveEntityTickable implements ActiveEntityTickable {
    protected final Entity entity;
    protected final EntityTickable tickable;
    protected final CruxSlot slot;

    public SimpleActiveEntityTickable(Entity entity, EntityTickable tickable, CruxSlot slot) {
        this.entity = entity;
        this.tickable = tickable;
        this.slot = slot;
    }

    @Override
    public @NotNull Entity getEntity() {
        return entity;
    }

    @Override
    public @NotNull EntityTickable getTickable() {
        return tickable;
    }

    @Override
    public @Nullable CruxSlot getSlot() {
        return slot;
    }
}
