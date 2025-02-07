package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActiveEntityTickable extends ManagedTicked {
    @NotNull Entity getEntity();
    @NotNull EntityTickable getTickable();
    @Nullable CruxSlot getSlot();
}
