package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntityTickable extends CruxKeyed {
    @Nullable ActiveEntityTickable buildActive(@NotNull Entity user, @Nullable CruxSlot slot, @NotNull EntityTickableModifier mod);
}
